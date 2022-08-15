package de.openknowledge;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.http.client.utils.URIBuilder;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class ReadClientTest {


    @ClassRule
    public static MySQLContainer<?> mysql = new MySQLContainer<>(
            "mysql:5.7.37").withInitScript("sql/init.sql")
            .withDatabaseName("servletDB")
            .withUsername("root")
            .withPassword("");


    @Test
    public void readClient() throws LifecycleException, URISyntaxException, IOException, InterruptedException {
        //Given


        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.setHostname("localhost");
        String appBase = ".";
        tomcat.getHost().setAppBase(appBase);

        File docBase = new File(System.getProperty("java.io.tmpdir"));
        Context context = tomcat.addContext("", docBase.getAbsolutePath());

        Repository repository = new Repository(mysql.getJdbcUrl(), mysql.getUsername(),mysql.getPassword());

        LoginServlet loginServlet = new LoginServlet(repository);

        Class servletClass = LoginServlet.class;
        Tomcat.addServlet(context, servletClass.getSimpleName(), loginServlet);
        context.addServletMappingDecoded("/LoginServlet/*", servletClass.getSimpleName());

        System.out.println("tomcat.start");
        tomcat.start();
        System.out.println("tomcat.getServer().await()");
        //tomcat.getServer().await();

        //When
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost:8080").setPath("/LoginServlet")
                .setParameter("fName", "TEST")
                .setParameter("lName", "TEST");
        URI uri = builder.build();

        HttpRequest request = HttpRequest.newBuilder(uri)
                .version(HttpClient.Version.HTTP_2)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        //Then
        assertEquals(response.statusCode(), HttpServletResponse.SC_OK);

        HttpRequest request2 = HttpRequest.newBuilder(uri)
                .version(HttpClient.Version.HTTP_2)
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        String responseBody2 = response2.body();
        assertEquals("Client{firstName='TEST', lastName='TEST'}", responseBody2);
        assertEquals(response2.statusCode(), HttpServletResponse.SC_OK);
    }
}
