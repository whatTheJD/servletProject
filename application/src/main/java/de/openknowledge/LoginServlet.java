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

        System.out.println("Clients:");
//        repository.readDb();
        repository.writeDB(fName, lName);

        response.getWriter().write("Your clients got updated!");
        response.getWriter().write("New client: " + fName + lName);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().flush();
        response.getWriter().close();

        System.out.println("Clients after insertion: ");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        List<Client> clientList = repository.readDb();
        for (Client client : clientList) {
            response.getWriter().write(client.toString());
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().flush();
        response.getWriter().close();
    }
}