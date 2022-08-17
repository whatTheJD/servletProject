package de.openknowledge;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "de.openknowledge.LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    RepositoryInterface repository;

    public LoginServlet(RepositoryInterface repository) {
        this.repository = repository;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fName = request.getParameter("fName");
        String lName = request.getParameter("lName");

        System.out.println("fName: " + fName);
        System.out.println("lName: " + lName);

        repository.saveClient(fName, lName);

        response.getWriter().write("Your clients got updated!");
        response.getWriter().write("New client: " + fName + " " + lName);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().flush();
        response.getWriter().close();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html");
        List<Client> clientList = repository.findClient();
        response.getWriter().write("<html><body><h1>List of Saved Clients</h1>");
        for (Client client : clientList) {
            response.getWriter().write(client.toString() + "<br>");
        }
        response.getWriter().write("</body></html>");
        response.getWriter().flush();
        response.getWriter().close();
    }
}