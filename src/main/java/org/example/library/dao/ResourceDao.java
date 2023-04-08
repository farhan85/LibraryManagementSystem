package org.example.library.dao;

import org.example.library.models.Resource;
import org.example.library.models.ResourceId;

/**
 * Convenience interface for classes that provides access to the data layer for storing Resource objects.
 *
 * This is the interface that business layer code should use when access to the data layer is needed.
 *
 * @param <I> The ResourceId type that identifies a resource.
 * @param <R> The type of Resource object to manage.
 */
public interface ResourceDao<I extends ResourceId, R extends Resource<I>> extends
        ResourceCreator<R>,
        ResourceDeleter<I>,
        ResourceRetriever<I, R>,
        ResourceScanner<R>,
        ResourceUpdater<R> {
}
