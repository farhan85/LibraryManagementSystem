package org.example.library.dao;

import com.google.inject.Inject;
import org.example.library.models.Resource;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implements the ResourceDao interface by delegating the operations to the respective Dao objects.
 *
 * @param <R> The type of Resource object to manage.
 */
public class ResourceDaoDelegator<R extends Resource> implements ResourceDao<R> {

    private final ResourceCreator<R> resourceCreator;
    private final ResourceDeleter<R> resourceDeleter;
    private final ResourceUpdater<R> resourceUpdater;
    private final ResourceRetriever<R> resourceRetriever;
    private final ResourceScanner<R> resourceScanner;

    @Inject
    public ResourceDaoDelegator(final ResourceCreator<R> resourceCreator,
                                final ResourceDeleter<R> resourceDeleter,
                                final ResourceUpdater<R> resourceUpdater,
                                final ResourceRetriever<R> resourceRetriever,
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
    public void delete(final UUID uuid) {
        resourceDeleter.delete(uuid);
    }

    @Override
    public Optional<R> get(final UUID uuid) {
        return resourceRetriever.get(uuid);
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
