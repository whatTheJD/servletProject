package de.openknowledge;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Repository {

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
