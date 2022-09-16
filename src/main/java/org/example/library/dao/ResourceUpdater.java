package org.example.library.dao;

import org.example.library.models.Resource;

/**
 * Implementations of this class are responsible for updating Resource objects to the database.
 *
 * @param <R> The type of Resource object to update.
 */
public interface ResourceUpdater<R extends Resource> {

    void update(R resource);
}
