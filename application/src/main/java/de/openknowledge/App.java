package de.openknowledge;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

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

        LoginServlet loginServlet = new LoginServlet(new Repository());

        Class servletClass = LoginServlet.class;
        Tomcat.addServlet(context, servletClass.getSimpleName(), loginServlet);
        context.addServletMappingDecoded("/LoginServlet/*", servletClass.getSimpleName());

        System.out.println("tomcat.start");
        tomcat.start();
        System.out.println("tomcat.getServer().await()");
        tomcat.getServer().await();

    }
}
