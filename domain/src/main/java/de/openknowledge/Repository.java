package de.openknowledge;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Repository implements RepositoryInterface {

    EntityManager entityManager;

    public Repository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void writeDB(String fName, String lName) {
        if(!entityManager.getTransaction().isActive()){
            entityManager.getTransaction().begin();
        }
        Client client = new Client(new Name(new FirstName(fName), new LastName(lName)));
        entityManager.persist(client);
        entityManager.getTransaction().commit();
        entityManager.clear();

        /*try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String query = ("INSERT INTO 'servletDB'.'members' ('id', 'firstName','lastName') "
                    + "VALUES ({0},{1},{2});");

            query = java.text.MessageFormat.format(query, "'2'", "'" + fName + "'", "'" + lName + "'");
            System.out.println(query);
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public List<Client> readDb(){
        List<Client> clientList = entityManager.createNamedQuery(Client.readAll).getResultList();

        /*try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM members");
            while (resultSet.next()){
//                System.out.println("ID: " + resultSet.getString("id"));
                clientList.add(new Client(resultSet.getString("id"), resultSet.getString("firstName"),resultSet.getString("lastName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        return clientList;
    }
}
