package de.openknowledge;

import java.util.List;

public interface RepositoryInterface {
    void writeDB(String fName, String lName);

    List<Client> readDb();
}
