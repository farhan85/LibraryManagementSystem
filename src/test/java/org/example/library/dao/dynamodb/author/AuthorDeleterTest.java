package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.document.Table;
import org.example.library.models.Author;
import org.example.library.testutils.AuthorFactory;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verify;

@Listeners(MockitoTestNGListener.class)
public class AuthorDeleterTest {

    private static final Author AUTHOR = AuthorFactory.random();

    @Mock
    private Table mockAuthorsTable;

    private AuthorDeleter authorDeleter;

    @BeforeMethod
    public void setup() {
        authorDeleter = new AuthorDeleter(mockAuthorsTable);
    }

    @Test
    public void GIVEN_uuid_WHEN_calling_delete_THEN_call_table_deleteItem_with_expected_arguments() {
        authorDeleter.delete(AUTHOR.getId());

        verify(mockAuthorsTable).deleteItem(AuthorAttributes.ID.toString(), AUTHOR.getId().value());
    }

    @Test
    public void GIVEN_Author_WHEN_calling_delete_THEN_call_table_deleteItem_with_expected_arguments() {
        authorDeleter.delete(AUTHOR.getId());

        verify(mockAuthorsTable).deleteItem(AuthorAttributes.ID.toString(), AUTHOR.getId().value());
    }
}
