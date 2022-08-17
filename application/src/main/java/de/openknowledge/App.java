package de.openknowledge;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.sql.SQLException;


public class App {
    public static void main(String[] args) throws LifecycleException, SQLException {

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.setHostname("localhost");
        String appBase = ".";
        tomcat.getHost().setAppBase(appBase);

        File docBase = new File(System.getProperty("java.io.tmpdir"));
        Context context = tomcat.addContext("", docBase.getAbsolutePath());


        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Customers to DB via JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        LoginServlet loginServlet = new LoginServlet(new Repository(entityManager)); //todo verletzt das nicht das layer-modell?


        Class servletClass = LoginServlet.class;
        Tomcat.addServlet(context, servletClass.getSimpleName(), loginServlet);
        context.addServletMappingDecoded("/LoginServlet/*", servletClass.getSimpleName());

        System.out.println("tomcat.start");
        tomcat.start();
        System.out.println("tomcat.getServer().await()");
        tomcat.getServer().await();
        entityManager.close();
        entityManagerFactory.close();

    }
}
