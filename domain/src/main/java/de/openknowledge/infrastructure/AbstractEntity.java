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

package de.openknowledge.infrastructure;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

/**
 * Base class for JPA entities. Correctly implements {@link #equals(Object)}, {@link #hashCode()} and {@link #toString()}.
 * @param I the type of the primary key
 */
@MappedSuperclass
public abstract class AbstractEntity<I> implements Serializable {

    protected abstract I getId();

    @Override
    public int hashCode() {
        if (getId() == null) {
            throw new IllegalStateException("Don't call hashCode() until the entity is persisted");
        }
        return getId().hashCode();
    }

    @Override
    public final boolean equals(final Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || !getClass().isAssignableFrom(object.getClass())) {
            return false;
        }

        AbstractEntity<I> entity = (AbstractEntity<I>)object;
        return getId() != null && getId().equals(entity.getId());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "#" + getId();
    }
}
