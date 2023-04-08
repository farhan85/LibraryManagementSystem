package org.example.library.models;

import java.util.UUID;

/**
 * Represents a resource ID.
 *
 * <p>
 * This interface is for wrapper ID classes to ensure we don't accidentally pass the wrong IDs to methods.
 * </p>
 */
public interface ResourceId {

    UUID uuid();

    /**
     * Convenience method to get this resource ID value as a string.
     * @return The resource ID as a string.
     */
    default String value() {
        return uuid().toString();
    }
}
