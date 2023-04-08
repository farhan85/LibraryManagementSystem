package org.example.library.dao;

import org.example.library.models.Resource;
import org.example.library.models.ResourceId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkState;

/**
 * An in-memory ResourceDao implementation.
 *
 * @param <I> The ResourceId type that identifies a resource.
 * @param <R> The type of Resource object to manage.
 */
public class LocalResourceDao<I extends ResourceId, R extends Resource<I>> implements ResourceDao<I, R> {

    private final Map<I, R> resources;

    public LocalResourceDao() {
        resources = new HashMap<>();
    }

    @Override
    public void create(final R resource) {
        final I resourceId = resource.getId();
        checkState(!resources.containsKey(resourceId), String.format("Resource already exists. ID=%s", resourceId));
        resources.put(resourceId, resource);
    }

    @Override
    public void delete(final I resourceId) {
        resources.remove(resourceId);
    }

    @Override
    public void scan(final Consumer<R> consumer) {
        resources.values().forEach(consumer);
    }

    @Override
    public Optional<R> get(final I resourceId) {
        return Optional.ofNullable(resources.getOrDefault(resourceId, null));
    }

    @Override
    public void update(final R resource) {
        final I resourceId = resource.getId();
        checkState(resources.containsKey(resourceId), String.format("Resource does not exist. ID=%s", resourceId));
        resources.put(resourceId, resource);
    }
}
