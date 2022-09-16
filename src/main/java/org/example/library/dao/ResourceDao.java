package org.example.library.dao;

import org.example.library.models.Resource;

/**
 * Convenience interface for classes that provides access to the data layer for storing Resource objects.
 *
 * This is the interface that business layer code should use when access to the data layer is needed.
 *
 * @param <R> The type of Resource object to manage.
 */
public interface ResourceDao<R extends Resource> extends ResourceCreator<R>, ResourceDeleter<R>,
        ResourceRetriever<R>, ResourceScanner<R>, ResourceUpdater<R> {
}
