package de.openknowledge;

import de.openknowledge.infrastructure.AbstractEntity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "TAB_CLIENT")
public class Client extends AbstractEntity<Integer>{
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id && name.equals(client.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
