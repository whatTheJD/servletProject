package de.openknowledge;

import javax.persistence.EntityManager;
import java.util.List;

public interface RepositoryInterface {


    void saveClient(String fName, String lName);

    List<Client> findClient();
}
