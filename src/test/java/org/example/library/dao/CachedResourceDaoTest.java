package org.example.library.dao;

import com.google.common.cache.CacheBuilderSpec;
import org.example.library.testutils.TestResource;
import org.example.library.testutils.TestResourceId;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class CachedResourceDaoTest {

    @Mock
    private VersionedResourceDao<TestResourceId, TestResource> mockResourceDao;
    @Mock
    private Consumer<TestResource> mockConsumer;

    private CacheBuilderSpec cacheBuilderSpec;
    private CachedResourceDao<TestResourceId, TestResource> cachedResourceDao;

    @BeforeMethod
    public void setup() {
        final String spec = "maximumSize=10000,expireAfterWrite=10m";
        cacheBuilderSpec = CacheBuilderSpec.parse(spec);
        cachedResourceDao = new CachedResourceDao<>(mockResourceDao, cacheBuilderSpec);
    }

    @Test
    public void GIVEN_testResource_WHEN_calling_create_THEN_call_resourceDao_create() {
        final TestResource expected = TestResource.random();
        cachedResourceDao.create(expected);
        verify(mockResourceDao).create(expected);
    }

    @Test
    public void GIVEN_testResource_WHEN_calling_create_then_get_THEN_do_not_call_resourceDao_get() {
        final TestResource expected = TestResource.of(TestResourceId.random(), 1);
        cachedResourceDao.create(expected);
        final Optional<TestResource> actual = cachedResourceDao.get(expected.getId(), 1);

        assertEquals(actual, Optional.of(expected));
        verify(mockResourceDao).create(expected);
        verifyNoMoreInteractions(mockResourceDao);
    }

    @Test
    public void GIVEN_resourceDao_throw_exception_WHEN_calling_get_THEN_return_empty_optional() {
        final TestResource testResource = TestResource.of(TestResourceId.random(), 1);
        final TestResourceId uuid = testResource.getId();
        final int dataVersion = testResource.getDataVersion();
        doThrow(RuntimeException.class).when(mockResourceDao)
                .get(uuid, dataVersion);

        assertEquals(cachedResourceDao.get(uuid, dataVersion), Optional.empty());
        verifyNoMoreInteractions(mockResourceDao);
    }

    @Test
    public void GIVEN_testResource_uuid_WHEN_calling_delete_THEN_call_resourceDao_delete() {
        final TestResource expected = TestResource.random();
        cachedResourceDao.delete(expected.getId());
        verify(mockResourceDao).delete(expected.getId());
    }

    @Test
    public void GIVEN_cachedResourceDao_WHEN_calling_list_THEN_call_resourceDao_list() {
        final List<TestResource> expected = List.of(TestResource.random(), TestResource.random());
        when(mockResourceDao.list()).thenReturn(expected);

        final List<TestResource> actual = cachedResourceDao.list();
        assertEquals(actual, expected);
    }

    @Test
    public void GIVEN_cachedResourceDao_WHEN_calling_scan_THEN_call_resourceDao_scan() {
        cachedResourceDao.scan(mockConsumer);
        verify(mockResourceDao).scan(mockConsumer);
    }

    @Test
    public void GIVEN_uuid_WHEN_calling_get_twice_THEN_call_resourceDao_get_twice() {
        final TestResource expected = TestResource.random();
        when(mockResourceDao.get(expected.getId())).thenReturn(Optional.of(expected));
        final Optional<TestResource> actual1 = cachedResourceDao.get(expected.getId());
        final Optional<TestResource> actual2 = cachedResourceDao.get(expected.getId());

        assertEquals(actual1, Optional.of(expected));
        assertEquals(actual2, Optional.of(expected));
        verify(mockResourceDao, times(2)).get(expected.getId());
    }

    @Test
    public void GIVEN_uuid_and_dataVersion_WHEN_calling_get_twice_THEN_call_resourceDao_get_once() {
        final TestResource expected = TestResource.random();
        when(mockResourceDao.get(expected.getId(), expected.getDataVersion())).thenReturn(Optional.of(expected));
        final Optional<TestResource> actual1 = cachedResourceDao.get(expected.getId(), expected.getDataVersion());
        final Optional<TestResource> actual2 = cachedResourceDao.get(expected.getId(), expected.getDataVersion());

        assertEquals(actual1, Optional.of(expected));
        assertEquals(actual2, Optional.of(expected));
        verify(mockResourceDao).get(expected.getId(), expected.getDataVersion());
    }

    @Test
    public void GIVEN_testResource_WHEN_calling_update_then_get_with_dataVersion_THEN_do_not_retrieve_latest_version_from_resourceDao() {
        final TestResource expected = TestResource.random();
        cachedResourceDao.update(expected);
        final Optional<TestResource> actual = cachedResourceDao.get(expected.getId(), expected.getDataVersion() + 1);

        assertEquals(actual, Optional.of(expected));
        verify(mockResourceDao).update(expected);
        verifyNoMoreInteractions(mockResourceDao);
    }
}
