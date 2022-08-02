package de.openknowledge;

import javax.persistence.Embeddable;

import static org.apache.commons.lang3.Validate.isTrue;

@Embeddable
public class LastName {
    private String value;

    public LastName(String name) {
        this.value = validate(name);
    }

    protected LastName() {
        // for JPA
    }

    protected String validate(String name) {
        isTrue(name.matches("[A-ZÄÖU0-9][a-zäöüß0-9-]+"), "name must only contain letters");
        return name.trim();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof LastName)) {
            return false;
        }
        LastName name = (LastName)object;
        return toString().equals(name.toString());
    }
}

