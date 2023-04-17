package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.example.library.models.Author;
import org.example.library.models.AuthorId;
import org.example.library.models.ImmutableAuthorId;
import org.example.library.testutils.AuthorFactory;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class AuthorRetrieverTest {

    private static final Author AUTHOR = AuthorFactory.random();
    private static final AuthorId INVALID_ID = ImmutableAuthorId.of(UUID.randomUUID());

    @Mock
    private Table mockAuthorsTable;
    @Mock
    private Function<Item, Author> mockItemToAuthorConverter;
    @Mock
    private Item mockAuthorItem;
    private AuthorRetriever authorRetriever;

    @BeforeMethod
    public void setup() {
        authorRetriever = new AuthorRetriever(mockAuthorsTable, mockItemToAuthorConverter);
    }

    @Test
    public void GIVEN_uuid_WHEN_calling_get_THEN_return_Author() {
        when(mockAuthorsTable.getItem(AuthorAttributes.ID.toString(), AUTHOR.getId().value()))
                .thenReturn(mockAuthorItem);
        when(mockItemToAuthorConverter.apply(mockAuthorItem)).thenReturn(AUTHOR);
        assertEquals(authorRetriever.get(AUTHOR.getId()), Optional.of(AUTHOR));
    }

    @Test
    public void GIVEN_uuid_and_item_does_not_exist_WHEN_calling_get_THEN_return_empty_optional() {
        when(mockAuthorsTable.getItem(AuthorAttributes.ID.toString(), INVALID_ID.value()))
                .thenReturn(null);
        assertEquals(authorRetriever.get(INVALID_ID), Optional.empty());
    }
}
