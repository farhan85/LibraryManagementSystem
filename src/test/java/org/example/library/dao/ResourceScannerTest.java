package org.example.library.dao;

import org.example.library.testutils.TestResource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.testng.AssertJUnit.assertEquals;

public class ResourceScannerTest {

    private List<TestResource> resources;
    private ResourceScanner<TestResource> resourceDao;

    @BeforeMethod
    public void setup() {
        resources = List.of(TestResource.random(), TestResource.random(), TestResource.random());
        resourceDao = new TestResourceDao(resources);
    }

    @Test
    public void GIVEN_resourceScanner_WHEN_calling_list_THEN_return_list_of_objects() {
        assertEquals(resourceDao.list(), resources);
    }

    private record TestResourceDao(List<TestResource> resources) implements ResourceScanner<TestResource> {

        @Override
        public void scan(final Consumer<TestResource> consumer) {
            resources.forEach(consumer);
        }
    }
}
