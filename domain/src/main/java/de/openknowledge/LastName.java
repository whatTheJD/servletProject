package de.openknowledge;

import static org.apache.commons.lang3.Validate.isTrue;

import de.openknowledge.infrastructure.AbstractSimpleValueObject;

import javax.persistence.Embeddable;


@Embeddable
public class LastName extends AbstractSimpleValueObject<String> {

    public LastName(String name) {
        super(name); //super ruft in diesem Fall den Konstruktor die Klasse AbstractSimpleValueObject auf
    }

    protected LastName() {
        //for JPA
    }


    @Override
    protected String validateAndNormalize(String value) {
        isTrue(value.matches("[A-ZÄÖU0-9][a-zäöüß0-9-]+"), "name must only contain letters");
        return super.validateAndNormalize(value);
    }
}

