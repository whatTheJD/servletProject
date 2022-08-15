/*
 * Copyright \(C\) open knowledge GmbH\.
 *
 * Licensed under the Apache License, Version 2\.0 \(the "License"\);
 * you may not use this file except in compliance with the License\.
 * You may obtain a copy of the License at
 *
 *     http://www\.apache\.org/licenses/LICENSE-2\.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied\.
 * See the License for the specific language governing permissions and
 * limitations under the License\.
 */

package de.openknowledge;

import static org.apache.commons.lang3.Validate.isTrue;

import javax.persistence.Embeddable;

import de.openknowledge.infrastructure.AbstractSimpleValueObject;



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

