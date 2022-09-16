package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.document.Table;
import org.example.library.models.Author;
import org.example.library.testutils.AuthorFactory;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@Listeners(MockitoTestNGListener.class)
public class AuthorDeleterTest {

    private static final Author AUTHOR = AuthorFactory.random();
    private static final UUID TEST_UUID = AUTHOR.getId();

    @Mock
    private Table mockAuthorsTable;

    private AuthorDeleter authorDeleter;

    @BeforeMethod
    public void setup() {
        authorDeleter = new AuthorDeleter(mockAuthorsTable);
    }

    @Test
    public void GIVEN_uuid_WHEN_calling_delete_THEN_call_table_deleteItem_with_expected_arguments() {
        authorDeleter.delete(TEST_UUID);

        verify(mockAuthorsTable).deleteItem(AuthorAttributes.ID.toString(), TEST_UUID.toString());
    }

    @Test
    public void GIVEN_Author_WHEN_calling_delete_THEN_call_table_deleteItem_with_expected_arguments() {
        authorDeleter.delete(AUTHOR);

        verify(mockAuthorsTable).deleteItem(AuthorAttributes.ID.toString(), TEST_UUID.toString());
    }
}
