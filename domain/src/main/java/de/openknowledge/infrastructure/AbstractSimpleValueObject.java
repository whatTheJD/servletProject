package de.openknowledge.infrastructure;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import java.io.Serializable;

import static javax.persistence.AccessType.FIELD;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * A base class for simple value objects like <tt>ZipCode</tt>, <tt>CityName</tt> and so on.
 * Concrete subclasses of this class may look like this:
 * <pre>
 * public class EmailAddress extends AbstractSimpleValueObject&lt;String> {
 *
 *   private static final String PATTERN = ...
 *
 *   protected EmailAddress() {
 *     // required for proxying
 *   }
 *
 *   public EmailAddress(String emailAddress) {
 *     super(emailAddress);
 *   }
 *
 *   protected String validateAndNormalize(String value) {
 *     matchesPattern(value, PATTERN, "%1 is not a valid email address", value);
 *   }
 * }
 * </pre>
 */
@MappedSuperclass
@Access(FIELD)
public abstract class AbstractSimpleValueObject<V extends Comparable<? super V>>
        implements Comparable<AbstractSimpleValueObject<V>>, Serializable {

    public static final String VALUE = "value";

    @Column(name = VALUE)
    private V value;

    protected AbstractSimpleValueObject() {
        // required for proxying
    }

    public AbstractSimpleValueObject(V initialValue) {
        value = validateAndNormalize(initialValue);
    }

    /**
     * Validates and normalizes the constructor parameter of this simple value object.
     * This implementation checks the value to be non-null.
     * Subclasses may override this method to alter validation and normalization.
     *
     * @param value The constructor value.
     * @return The validated and normalized value.
     */
    protected V validateAndNormalize(final V value) {
        return notNull(value, "value must not be null");
    }

    /**
     * Returns the simple value of this value object.
     * May be overridden by
     * @return the simple value
     */
    public V getValue() {
        return value;
    }

    public String toString() {
        return getValue().toString();
    }

    public int hashCode() {
        return getValue().hashCode();
    }

    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof AbstractSimpleValueObject)
                || !(object.getClass().isAssignableFrom(getClass()) || getClass().isAssignableFrom(object.getClass()))) {
            return false;
        }
        AbstractSimpleValueObject<V> valueObject = (AbstractSimpleValueObject<V>)object;
        return valueObject.getValue().equals(getValue());
    }

    @Override
    public int compareTo(AbstractSimpleValueObject<V> object) {
        return getValue().compareTo(object.getValue());
    }
}