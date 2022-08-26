package de.openknowledge;

import java.util.ArrayList;
import java.util.List;

public class RepositoryInMemory implements RepositoryInterface {

    //Fake implementierung
    ArrayList<String> liste = new ArrayList<>();

    @Override
    public void writeDB(String fName, String lName) {
        liste.add(fName);
        liste.add(lName);
    }

    @Override
    public List<Client> readDb() {
    return null;
    }
}