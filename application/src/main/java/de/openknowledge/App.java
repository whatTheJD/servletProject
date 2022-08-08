package de.openknowledge;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;

import javax.persistence.Persistence;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO Füge ok checkstyle und jacoco hinzu

public class App {
    public static void main(String[] args) throws Exception {

        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setResourceClasses(LoginResource.class);

        factoryBean.setResourceProvider(
                new SingletonResourceProvider(
                        new LoginResource(
                        new Repository(Persistence.createEntityManagerFactory("servletDB")
                        .createEntityManager()))));

        Map<Object, Object> extensionMappings = new HashMap<Object, Object>();
        extensionMappings.put("json", MediaType.APPLICATION_JSON);
        factoryBean.setExtensionMappings(extensionMappings);

        List<Object> providers = new ArrayList<Object>();
        providers.add(new JAXBElementProvider());
        providers.add(new JacksonJsonProvider());
        factoryBean.setProviders(providers);

        factoryBean.setAddress("http://localhost:8080/");

        Server server = factoryBean.create();

        System.out.println("Server ready...");
        Thread.sleep(60 * 1000);
        System.out.println("Server exiting");
        server.destroy();
        System.exit(0);
    }
}
