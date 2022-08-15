package de.openknowledge;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import de.openknowledge.infrastructure.AbstractEntity;


@Entity
@Table(name = "members")
@NamedQueries({@NamedQuery(name = Client.READ_ALL, query = "SELECT c from Client c")})
        //@NamedQuery(name = Client.readByName, query = "SELECT c from Client c where c.first=:firstName AND c.last=:lastName")})
@XmlRootElement(name = "Client")
public class Client extends AbstractEntity<Integer> {
    public static final String READ_ALL = "Client.readAll";
    public static final String READ_BY_NAME = "Client.readByName";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //TODO Beachte, dass die Variablen analog zu den ValueObjects benannt sein müssen!

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "firstName"))
    private FirstName firstName;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "lastName"))
    private LastName lastName;


    public Client(FirstName firstName, LastName lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    protected Client() {
        // for JPA
    }

    @Override
    protected Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName.getValue();
    }

    public String getLastName() {
        return lastName.getValue();
    }

}
