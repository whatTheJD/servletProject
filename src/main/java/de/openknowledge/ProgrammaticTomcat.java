package de.openknowledge;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URISyntaxException;
import java.util.Random;

/**
 * Created by adi on 1/10/18.
 */
public class ProgrammaticTomcat {

    private static boolean isFree(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static Tomcat tomcat = null;

    private static int randomPort;

    public ProgrammaticTomcat() {
        // Get a random port number in range 6000 (inclusive) - 9000 (exclusive)
        this.randomPort = new Random()
                .ints(6000, 9000)
                .filter(ProgrammaticTomcat::isFree)
                .findFirst()
                .orElse(8080);
    }

    // uncomment for live test
    public static void main(String[] args) throws LifecycleException, ServletException, URISyntaxException, IOException {
        startTomcat();
    }


    public int getPort() {
        return randomPort;
    }

    public static void startTomcat() throws LifecycleException {
        tomcat = new Tomcat();
        tomcat.setPort(randomPort);
        tomcat.setHostname("localhost");
        String appBase = ".";
        tomcat.getHost().setAppBase(appBase);

        File docBase = new File(System.getProperty("java.io.tmpdir"));
        Context context = tomcat.addContext("", docBase.getAbsolutePath());

        // add a servlet
        Class servletClass = MyServlet.class;
        Tomcat.addServlet(context, servletClass.getSimpleName(), servletClass.getName());
        context.addServletMappingDecoded("/my-servlet/*", servletClass.getSimpleName());

        // add a filter and filterMapping
        Class filterClass = MyFilter.class;
        FilterDef myFilterDef = new FilterDef();
        myFilterDef.setFilterClass(filterClass.getName());
        myFilterDef.setFilterName(filterClass.getSimpleName());
        context.addFilterDef(myFilterDef);

        FilterMap myFilterMap = new FilterMap();
        myFilterMap.setFilterName(filterClass.getSimpleName());
        myFilterMap.addURLPattern("/my-servlet/*");
        context.addFilterMap(myFilterMap);

        tomcat.start();
        // uncomment for live test
        tomcat.getServer().await();
        System.out.println("Started tomcat and got server");
    }

    public void stopTomcat() throws LifecycleException {
        tomcat.stop();
        tomcat.destroy();
    }
}
