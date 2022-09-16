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
        when(mockAuthorsTable.getItem(AuthorAttributes.ID.toString(), TEST_UUID.toString()))
                .thenReturn(AUTHOR_ITEM);
        authorRetriever = new AuthorRetriever(mockAuthorsTable);
    }

    @Test
    public void GIVEN_uuid_WHEN_calling_get_THEN_return_Author() {
        final Optional<Author> author = authorRetriever.get(TEST_UUID);
        assertEquals(author, Optional.of(AUTHOR));
    }
}
