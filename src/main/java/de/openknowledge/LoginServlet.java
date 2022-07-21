package de.openknowledge;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "de.openknowledge.LoginServlet", urlPatterns = { "/LoginServlet" })
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fName = request.getParameter("fName");
        String lName = request.getParameter("lName");

        System.out.println("Clients:");
        readDb();
        
        writeDB(fName,lName);

        response.getWriter().write("Your clients got updated!");
        response.getWriter().write("New client: " + fName + lName);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().flush();
        response.getWriter().close();

        System.out.println("Clients after insertion: ");
        readDb();
    }

    private void writeDB(String fName, String lName) {
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/servletDB", "testuser", "Test123456");
            Statement statement = connection.createStatement();
            String query = ("INSERT INTO 'servletDB'.'members' ('id', 'firstName','lastName') "
                    + "VALUES ({0},{1},{2});");

            query = java.text.MessageFormat.format(query, "'2'", "'" + fName + "'", "'" + lName + "'");
            System.out.println(query);
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void readDb(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/servletDB", "testuser", "Test123456");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM members");

            while (resultSet.next()){
                System.out.println("ID: " + resultSet.getString("id"));
                System.out.println("First Name: " + resultSet.getString("firstName"));
                System.out.println("Last Name: " + resultSet.getString("lastName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}