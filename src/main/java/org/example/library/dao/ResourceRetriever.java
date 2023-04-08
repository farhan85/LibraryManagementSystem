package org.example.library.dao;

import org.example.library.models.Resource;
import org.example.library.models.ResourceId;

import java.util.Optional;

/**
 * Implementations of this class are responsible for retrieving Resource objects from the database.
 *
 * @param <I> The ResourceId type that identifies a resource.
 * @param <R> The type of Resource object to retrieve.
 */
public interface ResourceRetriever<I extends ResourceId, R extends Resource<I>> {

    Optional<R> get(I resourceId);
}
