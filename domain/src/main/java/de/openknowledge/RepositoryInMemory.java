package de.openknowledge;

import java.sql.Array;
import java.util.ArrayList;

public class RepositoryInMemory implements RepositoryInterface {

    //Fake implementierung
    ArrayList<String> liste = new ArrayList<>();

    @Override
    public void writeDB(String fName, String lName) {
        liste.add(fName);
        liste.add(lName);
    }

    @Override
    public void readDb() {

    }
}
