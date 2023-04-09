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
public interface VersionedResourceRetriever<I extends ResourceId, R extends Resource<I>> {

    /**
     * Returns a specific version of a resource.
     * @param resourceId ID of the resource to retrieve.
     * @param dataVersion Data version of the resource to retrieve.
     * @return The specified resource.
     */
    Optional<R> get(I resourceId, int dataVersion);
}
