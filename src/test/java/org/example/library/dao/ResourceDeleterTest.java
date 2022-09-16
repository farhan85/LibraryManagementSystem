package org.example.library.dao;

import org.example.library.testutils.TestResource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ResourceDeleterTest {

    private TestDB mockDB;
    private ResourceDeleter<TestResource> resourceDao;

    @BeforeMethod
    public void setup() {
        mockDB = mock(TestDB.class);
        resourceDao = new TestResourceDao(mockDB);
    }

    @Test
    public void GIVEN_testResource_WHEN_calling_delete_THEN_call_delete_with_uuid() {
        final TestResource resource = TestResource.random();
        resourceDao.delete(resource);
        verify(mockDB).delete(resource.getId());
    }

    private interface TestDB {
        void delete(final UUID uuid);
    }

    private record TestResourceDao(TestDB testDB) implements ResourceDeleter<TestResource> {

        @Override
        public void delete(final UUID uuid) {
            testDB.delete(uuid);
        }
    }
}
