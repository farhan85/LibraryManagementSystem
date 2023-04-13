package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.Expected;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import org.example.library.dao.dynamodb.author.AuthorAttributes;
import org.example.library.models.Author;
import org.example.library.testutils.AuthorFactory;
import org.testng.annotations.Test;

import java.util.Collection;

import static org.example.library.testutils.AssertDdbObjects.assertDdbAttributeUpdates;
import static org.example.library.testutils.AssertDdbObjects.assertDdbExpectedCondition;
import static org.testng.Assert.assertEquals;

public class AuthorToItemConverterTest {

    private static final Author AUTHOR = AuthorFactory.random();
    private static final Item AUTHOR_ITEM = new Item()
            .withString(AuthorAttributes.ID.toString(), AUTHOR.getId().value())
            .withString(AuthorAttributes.FIRST_NAME.toString(), AUTHOR.getFirstName())
            .withString(AuthorAttributes.LAST_NAME.toString(), AUTHOR.getLastName())
            .withNumber(AuthorAttributes.DATA_VERSION.toString(), AUTHOR.getDataVersion());
    private static final Collection<KeyAttribute> PRIMARY_KEY_COMPONENTS = new PrimaryKey(
            AuthorAttributes.ID.toString(), AUTHOR.getId().value()).getComponents();
    private static final PutItemSpec PUT_ITEM_SPEC = new PutItemSpec()
            .withItem(new Item()
                    .withPrimaryKey(AuthorAttributes.ID.toString(), AUTHOR.getId().value())
                    .withString(AuthorAttributes.FIRST_NAME.toString(), AUTHOR.getFirstName())
                    .withString(AuthorAttributes.LAST_NAME.toString(), AUTHOR.getLastName())
                    .withNumber(AuthorAttributes.DATA_VERSION.toString(), AUTHOR.getDataVersion()))
            .withExpected(new Expected(AuthorAttributes.ID.toString()).notExist());
    private static final UpdateItemSpec UPDATE_ITEM_SPEC = new UpdateItemSpec()
            .withPrimaryKey(AuthorAttributes.ID.toString(), AUTHOR.getId().value())
            .withAttributeUpdate(
                    new AttributeUpdate(AuthorAttributes.FIRST_NAME.toString()).put(AUTHOR.getFirstName()),
                    new AttributeUpdate(AuthorAttributes.LAST_NAME.toString()).put(AUTHOR.getLastName()),
                    new AttributeUpdate(AuthorAttributes.DATA_VERSION.toString()).addNumeric(1))
            .withExpected(new Expected(AuthorAttributes.DATA_VERSION.toString()).eq(AUTHOR.getDataVersion()));

    @Test
    public void GIVEN_Item_with_Author_values_WHEN_calling_toAuthor_THEN_return_expected_Author() {
        assertEquals(AuthorToItemConverter.toAuthor(AUTHOR_ITEM), AUTHOR);
    }

    @Test
    public void GIVEN_Author_WHEN_calling_toPutItemSpec_THEN_return_expected_PutItemSpec() {
        final PutItemSpec actual = AuthorToItemConverter.toPutItemSpec(AUTHOR);
        assertEquals(actual.getItem(), PUT_ITEM_SPEC.getItem());
        assertDdbExpectedCondition(actual.getExpected(), PUT_ITEM_SPEC.getExpected());
    }

    @Test
    public void GIVEN_Author_WHEN_calling_toUpdateItemSpec_THEN_return_expected_UpdateItemSpec() {
        final UpdateItemSpec actual = AuthorToItemConverter.toUpdateItemSpec(AUTHOR);
        assertEquals(actual.getKeyComponents(), PRIMARY_KEY_COMPONENTS);
        assertDdbAttributeUpdates(actual.getAttributeUpdate(), UPDATE_ITEM_SPEC.getAttributeUpdate());
        assertDdbExpectedCondition(actual.getExpected(), UPDATE_ITEM_SPEC.getExpected());
    }
}
