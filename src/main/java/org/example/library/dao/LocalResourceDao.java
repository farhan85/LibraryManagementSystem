package org.example.library.dao;

import org.example.library.models.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * An in-memory ResourceDao implementation.
 *
 * @param <R> The type of Resource object to manage.
 */
public class LocalResourceDao<R extends Resource> implements ResourceDao<R> {

    private final Map<UUID, R> resources;

    public LocalResourceDao() {
        resources = new HashMap<>();
    }

    @Override
    public void create(final R resource) {
        if (!resources.containsKey(resource.getId())) {
            resources.put(resource.getId(), resource);
        } else {
            throw new RuntimeException(String.format("Resource already exists. ID=%s", resource.getId()));
        }
    }

    @Override
    public void delete(final UUID uuid) {
        resources.remove(uuid);
    }

    @Override
    public void scan(final Consumer<R> consumer) {
        resources.values().forEach(consumer);
    }

    @Override
    public Optional<R> get(final UUID uuid) {
        return Optional.ofNullable(resources.getOrDefault(uuid, null));
    }

    @Override
    public void update(final R resource) {
        resources.put(resource.getId(), resource);
    }
}
