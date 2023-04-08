package org.example.library.models;

/**
 * Represents a resource stored in the database.
 *
 * Every resource needs its own unique ID (using a UUID) and a DataVersion that
 * is used for optimistic locking.
 *
 * @param <I> The ResourceId type that identifies resource of this type.
 */
public interface Resource<I extends ResourceId> {

    I getId();

    int getDataVersion();
}
