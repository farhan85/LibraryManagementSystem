package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import org.example.library.models.Author;
import org.example.library.models.ImmutableAuthor;
import org.example.library.testutils.AuthorFactory;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.function.Function;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Listeners(MockitoTestNGListener.class)
public class AuthorCreatorTest {

    private static final Author AUTHOR = AuthorFactory.randomWithDataVersionOne();

    @Mock
    private Table mockAuthorsTable;
    @Mock
    private Function<Author, PutItemSpec> mockAuthorToPutItemSpecConverter;
    @Mock
    private PutItemSpec mockPutItemSpec;

    private AuthorCreator authorCreator;

    @BeforeMethod
    public void setup() {

        when(mockAuthorToPutItemSpecConverter.apply(AUTHOR)).thenReturn(mockPutItemSpec);
        when(mockPutItemSpec.withReturnValues(ReturnValue.NONE)).thenReturn(mockPutItemSpec);
        authorCreator = new AuthorCreator(mockAuthorsTable, mockAuthorToPutItemSpecConverter);
    }

    @Test
    public void GIVEN_Author_WHEN_calling_create_THEN_call_table_putItem_with_expected_arguments() {
        authorCreator.create(AUTHOR);
        verify(mockPutItemSpec).withReturnValues(ReturnValue.NONE);
        verify(mockAuthorsTable).putItem(mockPutItemSpec);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void GIVEN_Author_with_dataVersion_not_1_WHEN_calling_create_THEN_throw_IllegalArgumentException() {
        final Author author = ImmutableAuthor.copyOf(AUTHOR)
                .withDataVersion(5);
        authorCreator.create(author);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void GIVEN_Table_throws_ConditionalCheckFailedException_WHEN_calling_create_THEN_throw_RuntimeException() {
        doThrow(ConditionalCheckFailedException.class).when(mockAuthorsTable).putItem(mockPutItemSpec);
        authorCreator.create(AUTHOR);
    }
}
