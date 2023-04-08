package org.example.library.dao;

import org.example.library.testutils.TestResource;
import org.example.library.testutils.TestResourceId;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ResourceDeleterTest {

    private TestDB mockDB;
    private ResourceDeleter<TestResourceId> resourceDao;

    @BeforeMethod
    public void setup() {
        mockDB = mock(TestDB.class);
        resourceDao = new TestResourceDao(mockDB);
    }

    @Test
    public void GIVEN_testResource_WHEN_calling_delete_THEN_call_delete_with_uuid() {
        final TestResource resource = TestResource.random();
        resourceDao.delete(resource.getId());
        verify(mockDB).delete(resource.getId());
    }

    private interface TestDB {
        void delete(final TestResourceId resourceId);
    }

    private record TestResourceDao(TestDB testDB) implements ResourceDeleter<TestResourceId> {

        @Override
        public void delete(final TestResourceId resourceId) {
            testDB.delete(resourceId);
        }
    }
}
