package de.openknowledge;

import de.openknowledge.infrastructure.AbstractEntity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TAB_CLIENT")
public class Client extends AbstractEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO.IDENTITY)
    private int id;

    @Embedded
    private Name name;


    public Client(int id, Name name) {
        this.id = id;
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
}
