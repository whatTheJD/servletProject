package de.openknowledge;

import de.openknowledge.infrastructure.AbstractSimpleValueObject;

import javax.persistence.Embeddable;

import static org.apache.commons.lang3.Validate.isTrue;

@Embeddable
public class FirstName extends AbstractSimpleValueObject<String> {

    public FirstName(String name) {
        super(name);
    }

    protected FirstName() {
        //for JPA
    }


    @Override
    protected String validateAndNormalize(String value) {
        isTrue(value.matches("[A-ZÄÖU0-9][a-zäöüß0-9-]+"), "name must only contain letters");
        return super.validateAndNormalize(value);
    }
}
