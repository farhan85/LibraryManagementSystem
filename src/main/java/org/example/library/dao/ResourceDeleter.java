package org.example.library.dao;

import org.example.library.models.ResourceId;

/**
 * Implementations of this class are responsible for deleting Resource objects from the database.
 *
 * @param <I> The ResourceId type that identifies a resource.
 */
public interface ResourceDeleter<I extends ResourceId> {

    void delete(I resourceId);
}
