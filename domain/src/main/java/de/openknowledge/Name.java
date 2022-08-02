package de.openknowledge;


import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import static org.apache.commons.lang3.Validate.notNull;

@Embeddable
public class Name {

    @Embedded
    private FirstName first;

    @Embedded
    private LastName last;

    public Name(FirstName firstName, LastName lastName) {
        first = notNull(firstName, "first name may not be null");
        last = notNull(lastName, "last name may not be null");
    }

    protected Name() {
        // for JPA
    }

    public FirstName getFirst() {
        return first;
    }

    public LastName getLast() {
        return last;
    }

    @Override
    public String toString() {
        return first + " " + last;
    }

    @Override
    public int hashCode() {
        return first.hashCode() ^ last.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Name)) {
            return false;
        }
        Name name = (Name)object;
        return getFirst().equals(name.getFirst()) && getLast().equals(name.getLast());
    }
}

