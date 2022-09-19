package org.example.library.dao;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.example.library.testutils.TestResource;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class CachedResourceDaoTest {

    @Mock
    private ResourceDao<TestResource> mockResourceDao;
    @Mock
    private Cache<UUID, TestResource> mockCache;
    @Mock
    private Consumer<TestResource> mockConsumer;

    private CachedResourceDao<TestResource> cachedResourceDao;

    @BeforeMethod
    public void setup() {
        cachedResourceDao = new CachedResourceDao<>(mockResourceDao, mockCache);
    }

    @Test
    public void GIVEN_testResource_WHEN_calling_create_THEN_call_resourceDao_create() {
        final TestResource expected = TestResource.random();
        cachedResourceDao.create(expected);
        verify(mockResourceDao).create(expected);
        verifyNoInteractions(mockCache);
    }

    @Test
    public void GIVEN_testResource_uuid_WHEN_calling_delete_THEN_delete_with_resourceDao_and_remove_from_cache() {
        final TestResource expected = TestResource.random();
        cachedResourceDao.delete(expected.getId());
        verify(mockCache).invalidate(expected.getId());
        verify(mockResourceDao).delete(expected.getId());
    }

    @Test
    public void GIVEN_cachedResourceDao_WHEN_calling_list_THEN_call_resourceDao_list() {
        final List<TestResource> expected = List.of(TestResource.random(), TestResource.random());
        when(mockResourceDao.list()).thenReturn(expected);

        final List<TestResource> actual = cachedResourceDao.list();
        assertEquals(actual, expected);
        verifyNoInteractions(mockCache);
    }

    @Test
    public void GIVEN_cachedResourceDao_WHEN_calling_scan_THEN_call_resourceDao_scan() {
        cachedResourceDao.scan(mockConsumer);
        verify(mockResourceDao).scan(mockConsumer);
        verifyNoInteractions(mockCache);
    }

    @Test
    public void GIVEN_testResource_uuid_and_cache_has_testResource_WHEN_calling_get_THEN_retrieve_from_cache() {
        final TestResource expected = TestResource.random();
        final Cache<UUID, TestResource> cache = CacheBuilder.newBuilder().build();
        cache.put(expected.getId(), expected);
        cachedResourceDao = new CachedResourceDao<>(mockResourceDao, cache);

        assertEquals(cachedResourceDao.get(expected.getId()), Optional.of(expected));
        verifyNoInteractions(mockResourceDao);
    }

    @Test
    public void GIVEN_testResource_uuid_and_cache_is_empty_WHEN_calling_get_THEN_retrieve_from_resourceDao_and_store_in_cache() {
        final TestResource expected = TestResource.random();
        final Cache<UUID, TestResource> cache = CacheBuilder.newBuilder().build();
        cachedResourceDao = new CachedResourceDao<>(mockResourceDao, cache);
        when(mockResourceDao.get(expected.getId())).thenReturn(Optional.of(expected));

        assertEquals(cachedResourceDao.get(expected.getId()), Optional.of(expected));
        assertEquals(cache.asMap(), Map.of(expected.getId(), expected));
        verify(mockResourceDao).get(expected.getId());
    }

    @Test
    public void GIVEN_testResource_uuid_and_cache_has_obj_WHEN_calling_get_THEN_retrieve_from_cache() {
        final TestResource expected = TestResource.random();
        final Cache<UUID, TestResource> cache = CacheBuilder.newBuilder().build();
        cache.put(expected.getId(), expected);
        cachedResourceDao = new CachedResourceDao<>(mockResourceDao, cache);

        assertEquals(cachedResourceDao.get(expected.getId()), Optional.of(expected));
        verifyNoInteractions(mockResourceDao);
    }

    @Test
    public void GIVEN_testResource_WHEN_calling_update_THEN_invalidate_from_cache_and_update_with_resourceDao() {
        final TestResource expected = TestResource.random();
        cachedResourceDao.update(expected);
        verify(mockCache).invalidate(expected.getId());
        verify(mockResourceDao).update(expected);
    }
}
