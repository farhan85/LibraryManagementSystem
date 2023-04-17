package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import org.example.library.models.Author;
import org.example.library.testutils.AuthorFactory;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.ConcurrentModificationException;

import static org.example.library.dao.dynamodb.author.converter.AuthorToItemConverter.toUpdateItemSpec;
import static org.example.library.testutils.AssertDdbObjects.assertDdbAttributeUpdates;
import static org.example.library.testutils.AssertDdbObjects.assertDdbExpectedCondition;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class AuthorUpdaterTest {

    private static final Author AUTHOR = AuthorFactory.random();
    private static final Collection<KeyAttribute> PRIMARY_KEY_COMPONENTS = new PrimaryKey(
            AuthorAttributes.ID.toString(), AUTHOR.getId().value()).getComponents();
    private static final UpdateItemSpec UPDATE_ITEM_SPEC = toUpdateItemSpec(AUTHOR);

    @Mock
    private Table mockAuthorsTable;

    private AuthorUpdater authorUpdater;

    @BeforeMethod
    public void setup() {
        authorUpdater = new AuthorUpdater(mockAuthorsTable);
    }

    @Test
    public void GIVEN_Author_WHEN_calling_update_THEN_call_table_updateItem_with_expected_arguments() {
        final ArgumentCaptor<UpdateItemSpec> argumentCaptor = ArgumentCaptor.forClass(UpdateItemSpec.class);
        authorUpdater.update(AUTHOR);

        verify(mockAuthorsTable).updateItem(argumentCaptor.capture());
        final UpdateItemSpec updateItemSpec = argumentCaptor.getValue();
        assertEquals(updateItemSpec.getKeyComponents(), PRIMARY_KEY_COMPONENTS);
        assertDdbAttributeUpdates(updateItemSpec.getAttributeUpdate(), UPDATE_ITEM_SPEC.getAttributeUpdate());
        assertDdbExpectedCondition(updateItemSpec.getExpected(), UPDATE_ITEM_SPEC.getExpected());
        assertEquals(updateItemSpec.getReturnValues(), ReturnValue.NONE.toString());
    }

    @Test(expectedExceptions = ConcurrentModificationException.class)
    public void GIVEN_ddbClient_throws_ConditionalCheckFailedException_WHEN_calling_update_THEN_throw_ConcurrentModificationException() {
        doThrow(ConditionalCheckFailedException.class)
                .when(mockAuthorsTable).updateItem(any(UpdateItemSpec.class));
        authorUpdater.update(AUTHOR);
    }
}
