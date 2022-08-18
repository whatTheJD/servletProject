/*
 * Copyright (C) open knowledge GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.openknowledge;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

//TODO Logging einfügen

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


    public Optional<Client> getClient(String first, String last) {
        Optional<Client> client = entityManager.createNamedQuery(Client.READ_BY_NAME)
            .setParameter("firstName", first)
            .setParameter("lastName", last)
            .getResultStream().findAny();
        return client;
    }
}
