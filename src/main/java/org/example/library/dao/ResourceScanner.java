package org.example.library.dao;

import com.google.common.collect.ImmutableList;
import org.example.library.models.Resource;

import java.util.List;
import java.util.function.Consumer;

/**
 * Implementations of this class are responsible for iterating over all Resource objects in the database for processing.
 *
 * By implementing the scan method, the concrete class can use an efficient means to iterate over all objects while saving
 * memory by not requiring to store all objects in a list beforehand. If required, there is a list method that can be used
 * to return a list of all objects.
 *
 * @param <R> The type of Resource objects to scan for.
 */
public interface ResourceScanner<R extends Resource> {

    void scan(Consumer<R> consumer);

    default List<R> list() {
        final ImmutableList.Builder<R> items = ImmutableList.builder();
        scan(items::add);
        return items.build();
    }
}
