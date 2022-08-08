package de.openknowledge;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;
import org.apache.http.client.utils.URIBuilder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    private Client client;

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
    public void insertTestdata(){
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        client = new Client(new FirstName("Max"), new LastName("Mustermann"));
        entityManager.persist(client);
        entityManager.getTransaction().commit();
        entityManager.clear();
    }

    @Test
    public void readClient() throws LifecycleException, URISyntaxException, IOException, InterruptedException {
        //Given
        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setResourceClasses(LoginResource.class);

        factoryBean.setResourceProvider(
                new SingletonResourceProvider(
                        new LoginResource(
                                new Repository(entityManager))));

        Map<Object, Object> extensionMappings = new HashMap<Object, Object>();
        extensionMappings.put("json", MediaType.APPLICATION_JSON);
        factoryBean.setExtensionMappings(extensionMappings);

        List<Object> providers = new ArrayList<Object>();
        providers.add(new JAXBElementProvider());
        providers.add(new JacksonJsonProvider());
        factoryBean.setProviders(providers);

        factoryBean.setAddress("http://localhost:8080/");

        Server server = factoryBean.create();

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost:8080").setPath("/login/clients");
        URI uri = builder.build();

        //When
        HttpRequest request2 = HttpRequest.newBuilder(uri)
                .version(HttpClient.Version.HTTP_2)
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        String responseBody2 = response2.body();


        //Todo Passe Assert an
        //Then
        assertEquals("[{\"firstName\":\"Max\",\"lastName\":\"Mustermann\"}]", responseBody2);
        assertEquals(response2.statusCode(), HttpServletResponse.SC_OK);
        server.destroy();
        entityManager.close();
    }
}
