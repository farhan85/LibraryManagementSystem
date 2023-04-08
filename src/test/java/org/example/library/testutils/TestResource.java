package org.example.library.testutils;

import org.example.library.models.Resource;

import java.util.Random;

public final class TestResource implements Resource<TestResourceId> {

    private static final Random RANDOM = new Random();

    private final TestResourceId resourceId;
    private final int dataVersion;

    private TestResource(final TestResourceId resourceId, final int dataVersion) {
        this.resourceId = resourceId;
        this.dataVersion = dataVersion;
    }

    public static TestResource of(final TestResourceId resourceId, final int dataVersion) {
        return new TestResource(resourceId, dataVersion);
    }

    public static TestResource random() {
        return new TestResource(TestResourceId.random(), RANDOM.nextInt());
    }

    @Override
    public TestResourceId getId() {
        return resourceId;
    }

    @Override
    public int getDataVersion() {
        return dataVersion;
    }
}
