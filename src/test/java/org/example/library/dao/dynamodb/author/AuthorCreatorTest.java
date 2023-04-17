package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
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
    @Mock
    private PutItemSpec mockPutItemSpecWithReturnValue;

    private AuthorCreator authorCreator;

    @BeforeMethod
    public void setup() {
        authorCreator = new AuthorCreator(mockAuthorsTable, mockAuthorToPutItemSpecConverter);
    }

    @Test
    public void GIVEN_Author_WHEN_calling_create_THEN_call_table_putItem_with_expected_arguments() {
        when(mockAuthorToPutItemSpecConverter.apply(AUTHOR)).thenReturn(mockPutItemSpec);
        when(mockPutItemSpec.withReturnValues(ReturnValue.NONE)).thenReturn(mockPutItemSpecWithReturnValue);
        authorCreator.create(AUTHOR);
        verify(mockAuthorsTable).putItem(mockPutItemSpecWithReturnValue);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void GIVEN_Author_with_dataVersion_not_1_WHEN_calling_create_THEN_throw_IllegalArgumentException() {
        final Author author = ImmutableAuthor.copyOf(AUTHOR)
                .withDataVersion(5);
        authorCreator.create(author);
    }

}
