package de.openknowledge;

import org.hibernate.internal.build.AllowSysOut;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Repository implements RepositoryInterface {

    @Override
    public void writeDB(String fName, String lName) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Customers to DB via JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        entityTransaction.begin();
        Client client = new Client();
        client.setFirstName(fName);
        client.setLastName(lName);

        entityManager.persist(client);
        entityTransaction.commit();

        entityManager.close();
        entityManagerFactory.close();

        readDb();   //todo ABE artificial call in order to have readDB() executed at all
    }

    @Override
    public List<Client> readDb() {
        List<Client> clientList = new ArrayList<>();

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Customers to DB via JPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Client client = entityManager.find(Client.class, 102L);

        if (client != null) {
            System.out.println(client.toString());
        } else {
            System.out.println("no such object found");
        }

        return clientList;
    }
}
