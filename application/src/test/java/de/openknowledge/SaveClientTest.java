package de.openknowledge;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
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
public class SaveClientTest {

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
    public void createEntityManager(){
        entityManager = entityManagerFactory.createEntityManager();
    }

    //Todo Lösche @Disable um Test zu starten
    @Test
    public void saveClient() throws URISyntaxException, IOException, InterruptedException {
        //Given
        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setResourceClasses(LoginResource.class);

        entityManager.getTransaction().begin();

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

        //When
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost:8080").setPath("/login/clients");
        URI uri = builder.build();

        String body = "{\n" +
                "        \"firstName\": \"Test\",\n" +
                "        \"lastName\": \"Test\"\n" +
                "    }";

        HttpRequest request = HttpRequest.newBuilder(uri)
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        //Then
        assertEquals(response.statusCode(), HttpServletResponse.SC_OK);
        server.destroy();
        entityManager.close();
    }
}