package org.example.library.models;

import java.util.UUID;

/**
 * Represents a resource stored in the database.
 *
 * Every resource needs its own unique ID (using a UUID) and a DataVersion that
 * is used for optimistic locking.
 */
public interface Resource {

    UUID getId();

    int getDataVersion();
}
