package org.example.library.dao;

import org.example.library.models.Resource;

/**
 * Implementations of this class are responsible for storing Resource objects to the database.
 *
 * @param <R> The type of Resource object to create.
 */
public interface ResourceCreator<R extends Resource> {

    void create(R resource);
}
