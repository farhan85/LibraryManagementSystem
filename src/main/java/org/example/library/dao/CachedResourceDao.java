package org.example.library.dao;

import com.google.common.cache.Cache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.inject.Inject;
import org.example.library.models.Resource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A ResourceDao implementation with an in-memory cache.
 *
 * @param <R> The type of Resource object to manage.
 */
public class CachedResourceDao<R extends Resource> implements ResourceDao<R> {

    private final Cache<UUID, R> cache;
    private final ResourceDao<R> resourceDao;

    @Inject
    public CachedResourceDao(final ResourceDao<R> resourceDao, final Cache<UUID, R> cache) {
        this.resourceDao = checkNotNull(resourceDao);
        this.cache = checkNotNull(cache);
    }

    @Override
    public void create(final R resource) {
        resourceDao.create(resource);
    }

    @Override
    public void delete(final UUID uuid) {
        cache.invalidate(uuid);
        resourceDao.delete(uuid);
    }

    @Override
    public Optional<R> get(final UUID uuid) {
        try {
            final R resource = cache.get(uuid, () -> resourceDao.get(uuid).orElseThrow());
            return Optional.of(resource);
        } catch (final ExecutionException | UncheckedExecutionException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<R> list() {
        return resourceDao.list();
    }

    @Override
    public void scan(final Consumer<R> consumer) {
        resourceDao.scan(consumer);
    }

    @Override
    public void update(final R resource) {
        cache.invalidate(resource.getId());
        resourceDao.update(resource);
    }
}
