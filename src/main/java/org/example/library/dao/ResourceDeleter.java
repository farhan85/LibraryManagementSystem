package org.example.library.dao;

import org.example.library.models.Resource;

import java.util.UUID;

/**
 * Implementations of this class are responsible for deleting Resource objects from the database.
 *
 * @param <R> The type of Resource object to delete.
 */
public interface ResourceDeleter<R extends Resource> {

    void delete(UUID uuid);

    default void delete(final R resource) {
        delete(resource.getId());
    }
}
