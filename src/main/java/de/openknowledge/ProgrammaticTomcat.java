package de.openknowledge;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

/**
 * Created by adi on 1/10/18.
 */
public class ProgrammaticTomcat {
    public static void main(String[] args) throws LifecycleException {

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.setHostname("localhost");
        String appBase = ".";
        tomcat.getHost().setAppBase(appBase);

        File docBase = new File(System.getProperty("java.io.tmpdir"));
        Context context = tomcat.addContext("", docBase.getAbsolutePath());

        //add Servlet
        Class servletClass = LoginServlet.class;
        Tomcat.addServlet(context, servletClass.getSimpleName(), servletClass.getName());
        context.addServletMappingDecoded("/LoginServlet/*", servletClass.getSimpleName());

        System.out.println("Started Tomcat");
        tomcat.start();
        tomcat.getServer().await();
    }
}
