package org.example.library.dao;

import org.example.library.models.Resource;
import org.example.library.models.ResourceId;

/**
 * TODO
 * @param <I> The ResourceId type that identifies a resource.
 * @param <R> The type of Resource object to manage.
 */
public interface VersionedResourceDao<I extends ResourceId, R extends Resource<I>> extends
        ResourceDao<I, R>,
        VersionedResourceRetriever<I, R> {
}
