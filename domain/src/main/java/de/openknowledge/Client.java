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

//TODO Header automatisch per IDE einfügen

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


//TODO XMLRootElement Formdata verarbeiten - HTML Form Mapping

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
