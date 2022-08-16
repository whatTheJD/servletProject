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

import static org.apache.commons.lang3.Validate.notNull;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import de.openknowledge.infrastructure.AbstractValueObject;
@Embeddable
public class Name extends AbstractValueObject {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "firstName"))
    private FirstName first;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "lastName"))
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

    @Override
    protected Object[] values() {
        return new Object[0];
    }
}

