package de.openknowledge;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

public class Repository implements RepositoryInterface {
    EntityManager entityManager;
    public Repository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveClient(String fName, String lName) {

        EntityTransaction entityTransaction = entityManager.getTransaction();

        entityTransaction.begin();
        Client client = new Client();
        client.setFirstName(fName);
        client.setLastName(lName);

        entityManager.persist(client);
        entityTransaction.commit();
    }

    @Override
    public List<Client> findClient() {
        List<Client> clientList = new ArrayList<>();
        Client client;
        for (int i = 0; i < 1000; i = i + 1) {
            client = entityManager.find(Client.class, new Long(i));
            if (client != null) {
                clientList.add(client);
            }
        }
        return clientList;
    }
}