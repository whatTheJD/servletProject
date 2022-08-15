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
