package org.example.library.dao;

import com.google.common.cache.Cache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.inject.Inject;
import org.example.library.models.Resource;
import org.example.library.models.ResourceId;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A ResourceDao implementation with an in-memory cache.
 *
 * @param <I> The ResourceId type that identifies a resource.
 * @param <R> The type of Resource object to manage.
 */
public class CachedResourceDao<I extends ResourceId, R extends Resource<I>> implements ResourceDao<I, R> {

    private final Cache<I, R> cache;
    private final ResourceDao<I, R> resourceDao;

    @Inject
    public CachedResourceDao(final ResourceDao<I, R> resourceDao, final Cache<I, R> cache) {
        this.resourceDao = checkNotNull(resourceDao);
        this.cache = checkNotNull(cache);
    }

    @Override
    public void create(final R resource) {
        resourceDao.create(resource);
    }

    @Override
    public void delete(final I resourceId) {
        cache.invalidate(resourceId);
        resourceDao.delete(resourceId);
    }

    @Override
    public Optional<R> get(final I resourceId) {
        try {
            final R resource = cache.get(resourceId, () -> resourceDao.get(resourceId).orElseThrow());
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
