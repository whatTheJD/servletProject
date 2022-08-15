package de.openknowledge;

import java.util.List;

import javax.persistence.EntityManager;


public class Repository implements RepositoryInterface {

    EntityManager entityManager;

    public Repository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void writeDb(String fName, String lName) {
        Client client = new Client(new FirstName(fName), new LastName(lName));
        writeDb(client);
    }

    public void writeDb(Client client) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        entityManager.persist(client);
        entityManager.getTransaction().commit();
        entityManager.clear();
    }

    @Override
    public List<Client> readDb() {
        List<Client> clientList = entityManager.createNamedQuery(Client.READ_ALL).getResultList();
        return clientList;
    }


    /*public Optional<Client> getClient(String first, String last){
        Optional<Client> client = entityManager.createNamedQuery(Client.readByName)
        .setParameter("firstName", first)
        .setParameter("lastName", last)
        .getResultStream().findAny();
        return client;
    }*/
}
