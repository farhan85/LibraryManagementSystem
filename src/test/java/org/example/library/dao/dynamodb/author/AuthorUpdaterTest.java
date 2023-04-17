package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import org.example.library.models.Author;
import org.example.library.testutils.AuthorFactory;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ConcurrentModificationException;
import java.util.function.Function;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Listeners(MockitoTestNGListener.class)
public class AuthorUpdaterTest {

    private static final Author AUTHOR = AuthorFactory.random();

    @Mock
    private Table mockAuthorsTable;
    @Mock
    private Function<Author, UpdateItemSpec> mockAuthorToUpdateItemSpecConverter;
    @Mock
    private UpdateItemSpec mockUpdateItemSpec;
    @Mock
    private UpdateItemSpec mockUpdateItemSpecWithReturnValue;

    private AuthorUpdater authorUpdater;

    @BeforeMethod
    public void setup() {
        when(mockAuthorToUpdateItemSpecConverter.apply(AUTHOR)).thenReturn(mockUpdateItemSpec);
        when(mockUpdateItemSpec.withReturnValues(ReturnValue.NONE)).thenReturn(mockUpdateItemSpecWithReturnValue);
        authorUpdater = new AuthorUpdater(mockAuthorsTable, mockAuthorToUpdateItemSpecConverter);
    }

    @Test
    public void GIVEN_Author_WHEN_calling_update_THEN_call_table_updateItem_with_expected_arguments() {
        authorUpdater.update(AUTHOR);
        verify(mockAuthorsTable).updateItem(mockUpdateItemSpecWithReturnValue);
    }

    @Test(expectedExceptions = ConcurrentModificationException.class)
    public void GIVEN_ddbClient_throws_ConditionalCheckFailedException_WHEN_calling_update_THEN_throw_ConcurrentModificationException() {
        doThrow(ConditionalCheckFailedException.class)
                .when(mockAuthorsTable).updateItem(mockUpdateItemSpecWithReturnValue);
        authorUpdater.update(AUTHOR);
    }
}
