package de.openknowledge;

import de.openknowledge.infrastructure.AbstractEntity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "members")
@NamedQueries({@NamedQuery(name = Client.readAll, query = "SELECT c from Client c"),
        @NamedQuery(name = Client.readByName, query = "SELECT c from Client c where c.name =: name")})
@XmlRootElement(name = "Client")
public class Client extends AbstractEntity<Integer> {
    public static final String readAll = "Client.readAll";
    public static final String readByName = "Client.readByName";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Embedded
    private Name name;


    public Client(Name name) {
        this.name = name;
    }

    protected Client() {
        // for JPA
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    @Override
    protected Integer getId() {
        return id;
    }

    public String getNameAsString(){
        return name.getFirst().getValue() + " " + name.getLast().getValue();
    }
}
