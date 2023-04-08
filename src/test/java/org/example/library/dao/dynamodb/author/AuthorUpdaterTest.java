package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.google.common.collect.ImmutableMap;
import org.example.library.models.Author;
import org.example.library.testutils.AuthorFactory;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class AuthorUpdaterTest {

    private static final Author AUTHOR = AuthorFactory.random();
    private static final Collection<KeyAttribute> KEY_COMPONENTS = List.of(
            new KeyAttribute(AuthorAttributes.ID.toString(), AUTHOR.getId().value()));
    private static final String UPDATE_EXPRESSION = String.format("set %s,%s,%s",
            "#first_name = :first_name",
            "#last_name = :last_name",
            "#data_version = #data_version + :increment");
    private static final String CONDITION_EXPRESSION = "#data_version = :data_version";
    private static final Map<String, String> NAME_MAP = ImmutableMap.of(
            "#first_name", AuthorAttributes.FIRST_NAME.toString(),
            "#last_name", AuthorAttributes.LAST_NAME.toString(),
            "#data_version", AuthorAttributes.DATA_VERSION.toString());
    private static final Map<String, Object> VALUE_MAP = ImmutableMap.of(
            ":first_name", AUTHOR.getFirstName(),
            ":last_name", AUTHOR.getLastName(),
            ":data_version", BigDecimal.valueOf(AUTHOR.getDataVersion()),
            ":increment", BigDecimal.valueOf(1));

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
        assertEquals(updateItemSpec.getKeyComponents(), KEY_COMPONENTS);
        assertEquals(updateItemSpec.getUpdateExpression(), UPDATE_EXPRESSION);
        assertEquals(updateItemSpec.getConditionExpression(), CONDITION_EXPRESSION);
        assertEquals(updateItemSpec.getNameMap(), NAME_MAP);
        assertEquals(updateItemSpec.getValueMap(), VALUE_MAP);
    }

    @Test(expectedExceptions = ConcurrentModificationException.class)
    public void GIVEN_ddbClient_throws_ConditionalCheckFailedException_WHEN_calling_update_THEN_throw_ConcurrentModificationException() {
        doThrow(ConditionalCheckFailedException.class)
                .when(mockAuthorsTable).updateItem(any(UpdateItemSpec.class));
        authorUpdater.update(AUTHOR);
    }
}
