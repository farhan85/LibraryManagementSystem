package org.example.library.testutils;

import org.example.library.models.Resource;

import java.util.Random;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class TestResource implements Resource {

    private static final Random RANDOM = new Random();

    private final UUID uuid;
    private final int dataVersion;

    private TestResource(final UUID uuid, final int dataVersion) {
        this.uuid = checkNotNull(uuid);
        this.dataVersion = dataVersion;
    }

    public static TestResource of(final UUID uuid, final int dataVersion) {
        return new TestResource(uuid, dataVersion);
    }

    public static TestResource random() {
        return new TestResource(UUID.randomUUID(), RANDOM.nextInt());
    }

    @Override
    public UUID getId() {
        return uuid;
    }

    @Override
    public int getDataVersion() {
        return dataVersion;
    }
}
