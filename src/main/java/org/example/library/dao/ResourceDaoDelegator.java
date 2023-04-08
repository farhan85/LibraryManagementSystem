package org.example.library.dao;

import com.google.inject.Inject;
import org.example.library.models.Resource;
import org.example.library.models.ResourceId;

import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implements the ResourceDao interface by delegating the operations to the respective Dao objects.
 *
 * @param <I> The ResourceId type that identifies a resource.
 * @param <R> The type of Resource object to manage.
 */
public class ResourceDaoDelegator<I extends ResourceId, R extends Resource<I>> implements ResourceDao<I, R> {

    private final ResourceCreator<R> resourceCreator;
    private final ResourceDeleter<I> resourceDeleter;
    private final ResourceUpdater<R> resourceUpdater;
    private final ResourceRetriever<I, R> resourceRetriever;
    private final ResourceScanner<R> resourceScanner;

    @Inject
    public ResourceDaoDelegator(final ResourceCreator<R> resourceCreator,
                                final ResourceDeleter<I> resourceDeleter,
                                final ResourceUpdater<R> resourceUpdater,
                                final ResourceRetriever<I, R> resourceRetriever,
                                final ResourceScanner<R> resourceScanner) {
        this.resourceCreator = checkNotNull(resourceCreator);
        this.resourceDeleter = checkNotNull(resourceDeleter);
        this.resourceUpdater = checkNotNull(resourceUpdater);
        this.resourceRetriever = checkNotNull(resourceRetriever);
        this.resourceScanner = checkNotNull(resourceScanner);
    }

    @Override
    public void create(final R resource) {
        resourceCreator.create(resource);
    }

    @Override
    public void delete(final I resourceId) {
        resourceDeleter.delete(resourceId);
    }

    @Override
    public Optional<R> get(final I resourceId) {
        return resourceRetriever.get(resourceId);
    }

    @Override
    public void scan(final Consumer<R> consumer) {
        resourceScanner.scan(consumer);
    }

    @Override
    public void update(final R resource) {
        resourceUpdater.update(resource);
    }
}
