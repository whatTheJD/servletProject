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

        System.out.println("The following entries exist in the database before insertion:");
        readDB();

        writeDB(fName, lName);
        response.getWriter().write("The following entry has been added to the database:\n");
        response.getWriter().write("New user: " + fName + " " + lName);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().flush();
        response.getWriter().close();

        System.out.println("The following entries exist in the database after insertion:");
        readDB();
    }

    protected void writeDB(String firstname, String lastname) {     //todo ABE auto-increment id
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/serlvetDB", "ServletUser", "ForkIt");
            Statement statement = connection.createStatement();
            String query = ("INSERT INTO `serlvetDB`.`members` (`id`, `firstname`,`lastname`) "
                    + "VALUES ({0},{1},{2});");
            query = java.text.MessageFormat.format(query, "'2'", "'" + firstname + "'", "'" + lastname + "'"); //todo ABE make this nicer
            statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected void readDB() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/serlvetDB", "ServletUser", "ForkIt");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM members");

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getString("id"));
                System.out.println("Firstname: " + resultSet.getString("firstname"));
                System.out.println("Lastname: " + resultSet.getString("lastname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}