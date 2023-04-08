package org.example.library.testutils;

import org.example.library.models.ResourceId;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public final class TestResourceId implements ResourceId {

    private final UUID uuid;

    private TestResourceId(final UUID uuid) {
        this.uuid = checkNotNull(uuid);
    }

    public static TestResourceId of(final UUID uuid) {
        return new TestResourceId(uuid);
    }

    public static TestResourceId random() {
        return new TestResourceId(UUID.randomUUID());
    }

    @Override
    public UUID uuid() {
        return uuid;
    }
}
