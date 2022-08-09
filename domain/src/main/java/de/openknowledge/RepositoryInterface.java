package de.openknowledge;

import java.util.List;

public interface RepositoryInterface {
    void writeDb(String fName, String lName);

    List<Client> readDb();
}
