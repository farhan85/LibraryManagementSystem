package org.example.library.dao;

import org.example.library.models.Resource;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementations of this class are responsible for retrieving Resource objects from the database.
 *
 * @param <R> The type of Resource object to retrieve.
 */
public interface ResourceRetriever<R extends Resource> {

    Optional<R> get(UUID uuid);
}
