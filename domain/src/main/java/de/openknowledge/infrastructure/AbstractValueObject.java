package de.openknowledge.infrastructure;

import java.util.Arrays;

/**
 * A base class for value objects like <tt>City</tt>, <tt>PhoneNumber</tt> and so on.
 * This class provides implementations for {@link #equals(Object)} and {@link #hashCode()}
 * using the return value of {@link #values()}. Subclasses have to implement this method
 * and return the actual values of this value object.
 *
 * Concrete subclasses of this class may look like this:
 * <pre>
 * public class City extends AbstractValueObject {
 *
 *   private ZipCode zip;
 *   private CityName name;
 *
 *   protected City() {
 *     // required for proxying
 *   }
 *
 *   public City(ZipCode zipCode, CityName cityName) {
 *     zip = notNull(zipCode, "zip code may not be null");
 *     name = notNull(cityName, "city name may not be null");
 *   }
 *
 *   public ZipCode getZipCode() {
 *     return zip;
 *   }
 *
 *   public CityName getName() {
 *     return name;
 *   }
 *
 *   protected Object[] values() {
 *     return new Object[] { zip, name };
 *   }
 * }
 * </pre>
 */
public abstract class AbstractValueObject {

    private transient Object[] values;

    public int hashCode() {
        return Arrays.hashCode(getValues());
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof AbstractValueObject)
                || !(object.getClass().isAssignableFrom(getClass()) || getClass().isAssignableFrom(object.getClass()))) {
            return false;
        }
        AbstractValueObject valueObject = (AbstractValueObject)object;
        return Arrays.equals(getValues(), valueObject.getValues());
    }

    /**
     * Returns all values of this value object
     * Subclasses have to implement this method
     * and return the actual values that make up this value object.
     */
    protected abstract Object[] values();

    private Object[] getValues() {
        if (values == null) {
            values = values();
        }
        return values;
    }
}
