package org.example.library.dao;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
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
public class CachedResourceDao<I extends ResourceId, R extends Resource<I>> implements VersionedResourceDao<I, R> {

    private final Cache<String, R> cache;
    private final VersionedResourceDao<I, R> resourceDao;

    @Inject
    public CachedResourceDao(final VersionedResourceDao<I, R> resourceDao, final CacheBuilderSpec cacheBuilderSpec) {
        this.resourceDao = checkNotNull(resourceDao);
        this.cache = CacheBuilder.from(checkNotNull(cacheBuilderSpec)).build();
    }

    @Override
    public void create(final R resource) {
        resourceDao.create(resource);
        final String key = generateKey(resource.getId(), resource.getDataVersion());
        cache.put(key, resource);
    }

    @Override
    public void delete(final I resourceId) {
        resourceDao.delete(resourceId);
    }

    @Override
    public Optional<R> get(final I resourceId) {
        return resourceDao.get(resourceId)
                .map(resource -> {
                    final String key = generateKey(resourceId, resource.getDataVersion());
                    cache.put(key, resource);
                    return resource;
                });
    }

    @Override
    public Optional<R> get(final I resourceId, final int dataVersion) {
        try {
            final String key = generateKey(resourceId, dataVersion);
            final R resource = cache.get(key, () -> resourceDao.get(resourceId, dataVersion).orElseThrow());
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
        resourceDao.update(resource);
        final String key = generateKey(resource.getId(), resource.getDataVersion() + 1);
        cache.put(key, resource);
    }

    private String generateKey(final I resourceId, final int dataVersion) {
        return String.format("%s_%d", resourceId.value(), dataVersion);
    }
}
