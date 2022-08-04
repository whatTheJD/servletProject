package de.openknowledge;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.http.client.utils.URIBuilder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class ReadClientTest {

    @Container
    public static MySQLContainer<?> mysql = new MySQLContainer<>(
            "mysql:5.7.37").withInitScript("sql/init.sql")
            .withDatabaseName("servletDB")
            .withUsername("root")
            .withPassword("");


    //TODO Nutze die URL vom TestContainer, statt der "richtigen" URL

    //private Client client;

    /*@BeforeEach
    public void insertTestdata(){
        client = new Client(new Name(new FirstName("Max"), new LastName("Mustermann")));
        entityManager.persist(client);
        entityManager.getTransaction().commit();
        entityManager.clear();
    }*/

    static EntityManagerFactory entityManagerFactory;
    EntityManager entityManager;

    //In diesem Schritt wird dem EntityManager explizit der TestContainer zugewiesen
    @BeforeAll
    public static void createEntityManagerFactory(){
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
        properties.put("javax.persistence.jdbc.url", mysql.getJdbcUrl());
        properties.put("javax.persistence.jdbc.user", mysql.getUsername());
        properties.put("javax.persistence.jdbc.password", mysql.getPassword());
        properties.put("javax.persistence.schema-generation.database.action", "drop-and-create");
        properties.put("javax.persistence.jdbc.show_sql", "true");
        entityManagerFactory = Persistence.createEntityManagerFactory("servletDB", properties);
    }

    @BeforeEach
    public void createEntityManager(){
        entityManager = entityManagerFactory.createEntityManager();
    }

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

        Repository repository = new Repository(entityManager);

        LoginServlet loginServlet = new LoginServlet(repository);

        Class servletClass = LoginServlet.class;
        Tomcat.addServlet(context, servletClass.getSimpleName(), loginServlet);
        context.addServletMappingDecoded("/LoginServlet/*", servletClass.getSimpleName());

        System.out.println("tomcat.start");
        tomcat.start();
        System.out.println("tomcat.getServer().await()");
        //tomcat.getServer().await();

        //Todo Testdaten vorher übergeben
        entityManager.getTransaction().begin();



        //When
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost:8080").setPath("/LoginServlet")
                .setParameter("fName", "Test")
                .setParameter("lName", "Test");
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

        assertEquals("Test Test", responseBody2);
        assertEquals(response2.statusCode(), HttpServletResponse.SC_OK);
        entityManager.close();
    }
}
