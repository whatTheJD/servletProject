package de.openknowledge;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

        repository.writeDb(fName, lName);

        response.getWriter().write("Your clients got updated!");
        response.getWriter().write("New client: " + fName + lName);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().flush();
        response.getWriter().close();

    }

/*    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        List<Client> clientList = repository.readDb();
        for (Client client : clientList) {
            response.getWriter().write(client.getNameAsString());
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().flush();
        response.getWriter().close();
    }*/
}