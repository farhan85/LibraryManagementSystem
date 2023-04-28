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

    @Mock
    private Table mockAuthorsTable;

    private AuthorDeleter authorDeleter;

    @BeforeMethod
    public void setup() {
        authorDeleter = new AuthorDeleter(mockAuthorsTable);
    }

    @Test
    public void GIVEN_authorId_WHEN_calling_delete_THEN_call_table_deleteItem_with_expected_arguments() {
        final Author author = AuthorFactory.random();
        authorDeleter.delete(author.getId());
        verify(mockAuthorsTable).deleteItem(AuthorAttributes.ID.toString(), author.getId().value());
    }
}
