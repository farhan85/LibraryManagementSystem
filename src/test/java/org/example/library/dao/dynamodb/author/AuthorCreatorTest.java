package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.document.Expected;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.google.common.collect.ImmutableList;
import org.example.library.models.Author;
import org.example.library.models.ImmutableAuthor;
import org.example.library.testutils.AuthorFactory;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Collection;

import static org.example.library.testutils.AssertDdbObjects.assertDdbExpectedCondition;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class AuthorCreatorTest {

    private static final Author AUTHOR = AuthorFactory.randomWithDataVersionOne();
    private static final Item AUTHOR_ITEM = new Item()
            .withString(AuthorAttributes.ID.toString(), AUTHOR.getId().value())
            .withString(AuthorAttributes.FIRST_NAME.toString(), AUTHOR.getFirstName())
            .withString(AuthorAttributes.LAST_NAME.toString(), AUTHOR.getLastName())
            .withNumber(AuthorAttributes.DATA_VERSION.toString(), AUTHOR.getDataVersion());
    private static final Collection<Expected> EXPECTED_CONDITION = ImmutableList.of(
            new Expected(AuthorAttributes.ID.toString()).notExist());

    @Mock
    private Table mockAuthorsTable;

    private AuthorCreator authorCreator;

    @BeforeMethod
    public void setup() {
        authorCreator = new AuthorCreator(mockAuthorsTable);
    }

    @Test
    public void GIVEN_Author_WHEN_calling_create_THEN_call_table_putItem_with_expected_arguments() {
        final ArgumentCaptor<PutItemSpec> argumentCaptor = ArgumentCaptor.forClass(PutItemSpec.class);
        authorCreator.create(AUTHOR);

        verify(mockAuthorsTable).putItem(argumentCaptor.capture());
        final PutItemSpec putItemSpec = argumentCaptor.getValue();
        assertEquals(putItemSpec.getItem(), AUTHOR_ITEM);
        assertDdbExpectedCondition(putItemSpec.getExpected(), EXPECTED_CONDITION);
        assertEquals(putItemSpec.getReturnValues(), ReturnValue.NONE.toString());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void GIVEN_Author_with_dataVersion_not_1_WHEN_calling_create_THEN_throw_IllegalArgumentException() {
        final Author author = ImmutableAuthor.copyOf(AUTHOR)
                .withDataVersion(5);
        authorCreator.create(author);
    }

}
