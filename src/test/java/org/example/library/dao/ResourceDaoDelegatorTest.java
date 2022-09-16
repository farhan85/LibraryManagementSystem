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
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class ResourceDaoDelegatorTest {

    @Mock
    private ResourceCreator<TestResource> mockResourceCreator;
    @Mock
    private ResourceDeleter<TestResource> mockResourceDeleter;
    @Mock
    private ResourceUpdater<TestResource> mockResourceUpdater;
    @Mock
    private ResourceRetriever<TestResource> mockResourceRetriever;
    @Mock
    private ResourceScanner<TestResource> mockResourceScanner;
    @Mock
    private Consumer<TestResource> mockConsumer;

    private ResourceDaoDelegator<TestResource> resourceDao;

    @BeforeMethod
    public void setup() {
        resourceDao = new ResourceDaoDelegator<>(mockResourceCreator, mockResourceDeleter, mockResourceUpdater, mockResourceRetriever, mockResourceScanner);
    }

    @Test
    public void GIVEN_resource_WHEN_calling_create_THEN_call_resourceCreator_create() {
        final TestResource resource = TestResource.random();
        resourceDao.create(resource);
        verify(mockResourceCreator).create(resource);
    }

    @Test
    public void GIVEN_resource_uuid_WHEN_calling_delete_THEN_call_resourceDeleter_delete() {
        final TestResource resource = TestResource.random();
        resourceDao.delete(resource.getId());
        verify(mockResourceDeleter).delete(resource.getId());
    }

    @Test
    public void GIVEN_resource_uuid_WHEN_calling_get_THEN_return_resource_obj() {
        final TestResource resource = TestResource.random();
        when(mockResourceRetriever.get(resource.getId())).thenReturn(Optional.of(resource));
        assertEquals(resourceDao.get(resource.getId()), Optional.of(resource));
    }

    @Test
    public void GIVEN_resource_WHEN_calling_scan_THEN_call_resourceScanner_scan() {
        resourceDao.scan(mockConsumer);
        verify(mockResourceScanner).scan(mockConsumer);
    }

    @Test
    public void GIVEN_resource_WHEN_calling_update_THEN_call_resourceUpdater_update() {
        final TestResource resource = TestResource.random();
        resourceDao.update(resource);
        verify(mockResourceUpdater).update(resource);
    }
}
