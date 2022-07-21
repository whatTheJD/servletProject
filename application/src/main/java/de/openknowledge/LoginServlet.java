package de.openknowledge;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "de.openknowledge.LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fName = request.getParameter("fName");
        String lName = request.getParameter("lName");

        System.out.println("fName: " + fName);
        System.out.println("lName: " + lName);

        response.getWriter().write("Hello " + fName + " " + lName);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().flush();
        response.getWriter().close();

        Repository repository = new Repository();
        repository.readDb();
        repository.writeDB(fName, lName);
        repository.readDb();
    }
}