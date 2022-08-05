package de.openknowledge;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Repository implements RepositoryInterface {

    EntityManager entityManager;

    public Repository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void writeDB(String fName, String lName) {
        Client client = new Client(new FirstName(fName), new LastName(lName));
        writeDB(client);
    }

    public void writeDB(Client client) {
        if(!entityManager.getTransaction().isActive()){
            entityManager.getTransaction().begin();
        }
        entityManager.persist(client);
        entityManager.getTransaction().commit();
        entityManager.clear();
    }

    @Override
    public List<Client> readDb(){
        List<Client> clientList = entityManager.createNamedQuery(Client.readAll).getResultList();
        return clientList;
    }


    /*public Optional<Client> getClient(String first, String last){
        Optional<Client> client = entityManager.createNamedQuery(Client.readByName).setParameter("firstName", first).setParameter("lastName", last).getResultStream().findAny();
        return client;
    }*/
}
