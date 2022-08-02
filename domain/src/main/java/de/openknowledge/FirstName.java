package de.openknowledge;

import javax.persistence.Embeddable;

import static org.apache.commons.lang3.Validate.isTrue;

@Embeddable
public class FirstName {

    private String value;

    public FirstName(String name) {
        this.value = validate(name);
    }

    protected FirstName() {
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
        if (!(object instanceof FirstName)) {
            return false;
        }
        FirstName name = (FirstName)object;
        return toString().equals(name.toString());
    }
}
