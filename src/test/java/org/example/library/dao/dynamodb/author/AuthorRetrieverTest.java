package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.example.library.models.Author;
import org.example.library.testutils.AuthorFactory;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class AuthorRetrieverTest {

    private static final Author AUTHOR = AuthorFactory.random();
    private static final UUID INVALID_UUID = UUID.randomUUID();
    private static final UUID TEST_UUID = AUTHOR.getId();
    private static final Item AUTHOR_ITEM = new Item()
            .withString(AuthorAttributes.ID.toString(), TEST_UUID.toString())
            .withString(AuthorAttributes.FIRST_NAME.toString(), AUTHOR.getFirstName())
            .withString(AuthorAttributes.LAST_NAME.toString(), AUTHOR.getLastName())
            .withNumber(AuthorAttributes.DATA_VERSION.toString(), AUTHOR.getDataVersion());

    @Mock
    private Table mockAuthorsTable;
    private AuthorRetriever authorRetriever;

    @BeforeMethod
    public void setup() {
        authorRetriever = new AuthorRetriever(mockAuthorsTable);
    }

    @Test
    public void GIVEN_uuid_WHEN_calling_get_THEN_return_Author() {
        when(mockAuthorsTable.getItem(AuthorAttributes.ID.toString(), TEST_UUID.toString()))
                .thenReturn(AUTHOR_ITEM);
        assertEquals(authorRetriever.get(TEST_UUID), Optional.of(AUTHOR));
    }

    @Test
    public void GIVEN_uuid_and_item_does_not_exist_WHEN_calling_get_THEN_return_empty_optional() {
        when(mockAuthorsTable.getItem(AuthorAttributes.ID.toString(), INVALID_UUID.toString()))
                .thenReturn(null);
        assertEquals(authorRetriever.get(INVALID_UUID), Optional.empty());
    }
}
