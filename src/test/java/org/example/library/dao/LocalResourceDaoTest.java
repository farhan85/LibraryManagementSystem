package org.example.library.dao;

import org.example.library.testutils.TestResource;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.function.Consumer;

import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class LocalResourceDaoTest {

    @Mock
    private Consumer<TestResource> mockConsumer;

    private LocalResourceDao<TestResource> resourceDao;

    @BeforeMethod
    public void setup() {
        resourceDao = new LocalResourceDao<>();
    }

    @Test
    public void GIVEN_resource_WHEN_calling_create_THEN_call_resourceCreator_create() {
        final TestResource resource = TestResource.random();
        resourceDao.create(resource);
        assertEquals(resourceDao.get(resource.getId()), Optional.of(resource));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void GIVEN_resource_already_exists_WHEN_calling_create_THEN_throw_RuntimeException() {
        final TestResource resource = TestResource.random();
        resourceDao.create(resource);
        resourceDao.create(resource);
    }

    @Test
    public void GIVEN_resource_uuid_WHEN_calling_delete_THEN_call_resourceDeleter_delete() {
        final TestResource resource = TestResource.random();
        resourceDao.delete(resource.getId());
        assertEquals(resourceDao.get(resource.getId()), Optional.empty());
    }

    @Test
    public void GIVEN_resource_WHEN_calling_scan_THEN_call_resourceScanner_scan() {
        final TestResource resource1 = TestResource.random();
        final TestResource resource2 = TestResource.random();
        resourceDao.create(resource1);
        resourceDao.create(resource2);

        resourceDao.scan(mockConsumer);
        verify(mockConsumer).accept(resource1);
        verify(mockConsumer).accept(resource2);
    }

    @Test
    public void GIVEN_resource_WHEN_calling_update_with_new_dataVersion_THEN_save_resource_with_new_dataVersion() {
        final TestResource resource1 = TestResource.random();
        final TestResource resource2 = TestResource.of(resource1.getId(), resource1.getDataVersion() + 1);
        resourceDao.create(resource1);
        resourceDao.update(resource2);
        assertEquals(resourceDao.get(resource1.getId()), Optional.of(resource2));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void GIVEN_resource_not_exists_WHEN_calling_update_THEN_throw_IllegalStateException() {
        final TestResource resource1 = TestResource.random();
        resourceDao.update(resource1);
    }
}
