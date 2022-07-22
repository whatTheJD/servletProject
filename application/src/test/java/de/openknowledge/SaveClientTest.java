package de.openknowledge;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SaveClientTest {

    @Test
    public void saveClient() throws LifecycleException, URISyntaxException, IOException, InterruptedException {
        //Given
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.setHostname("localhost");
        String appBase = ".";
        tomcat.getHost().setAppBase(appBase);

        File docBase = new File(System.getProperty("java.io.tmpdir"));
        Context context = tomcat.addContext("", docBase.getAbsolutePath());

        LoginServlet loginServlet = new LoginServlet(new RepositoryInMemory());

        Class servletClass = LoginServlet.class;
        Tomcat.addServlet(context, servletClass.getSimpleName(), loginServlet);
        context.addServletMappingDecoded("/LoginServlet/*", servletClass.getSimpleName());

        System.out.println("tomcat.start");
        tomcat.start();
        System.out.println("tomcat.getServer().await()");
//        tomcat.getServer().await();

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
    }
}
