package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.Expected;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import org.example.library.models.Author;
import org.example.library.testutils.AuthorFactory;
import org.testng.annotations.Test;

import java.util.Collection;

import static org.example.library.dao.dynamodb.author.AuthorAttributes.DATA_VERSION;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.EMAIL;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.FIRST_NAME;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.ID;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.LAST_NAME;
import static org.example.library.testutils.AssertDdbObjects.assertDdbAttributeUpdates;
import static org.example.library.testutils.AssertDdbObjects.assertDdbExpectedCondition;
import static org.testng.Assert.assertEquals;

public class AuthorToItemSpecConverterTest {

    private static final Author AUTHOR_NON_OPT = AuthorFactory.randomNonOptionals();
    private static final Author AUTHOR_FULL = AuthorFactory.random();
    private static final Collection<KeyAttribute> PRIMARY_KEY_COMPONENTS_NON_OPT = new PrimaryKey(
            ID.toString(), AUTHOR_NON_OPT.getId().value()).getComponents();
    private static final Collection<KeyAttribute> PRIMARY_KEY_COMPONENTS_FULL = new PrimaryKey(
            ID.toString(), AUTHOR_FULL.getId().value()).getComponents();
    private static final PutItemSpec PUT_ITEM_SPEC_NON_OPT = new PutItemSpec()
            .withItem(new Item()
                    .withPrimaryKey(ID.toString(), AUTHOR_NON_OPT.getId().value())
                    .withString(FIRST_NAME.toString(), AUTHOR_NON_OPT.getFirstName())
                    .withString(LAST_NAME.toString(), AUTHOR_NON_OPT.getLastName())
                    .withNumber(DATA_VERSION.toString(), AUTHOR_NON_OPT.getDataVersion()))
            .withExpected(new Expected(ID.toString()).notExist());
    private static final PutItemSpec PUT_ITEM_SPEC_FULL = new PutItemSpec()
            .withItem(new Item()
                    .withPrimaryKey(ID.toString(), AUTHOR_FULL.getId().value())
                    .withString(FIRST_NAME.toString(), AUTHOR_FULL.getFirstName())
                    .withString(LAST_NAME.toString(), AUTHOR_FULL.getLastName())
                    .withNumber(DATA_VERSION.toString(), AUTHOR_FULL.getDataVersion())
                    .withString(EMAIL.toString(), AUTHOR_FULL.getEmail().orElseThrow().value()))
            .withExpected(new Expected(ID.toString()).notExist());
    private static final UpdateItemSpec UPDATE_ITEM_SPEC_NON_OPT = new UpdateItemSpec()
            .withPrimaryKey(ID.toString(), AUTHOR_NON_OPT.getId().value())
            .withAttributeUpdate(
                    new AttributeUpdate(FIRST_NAME.toString()).put(AUTHOR_NON_OPT.getFirstName()),
                    new AttributeUpdate(LAST_NAME.toString()).put(AUTHOR_NON_OPT.getLastName()),
                    new AttributeUpdate(DATA_VERSION.toString()).addNumeric(1))
            .withExpected(new Expected(DATA_VERSION.toString()).eq(AUTHOR_NON_OPT.getDataVersion()));
    private static final UpdateItemSpec UPDATE_ITEM_SPEC_FULL = new UpdateItemSpec()
            .withPrimaryKey(ID.toString(), AUTHOR_FULL.getId().value())
            .withAttributeUpdate(
                    new AttributeUpdate(FIRST_NAME.toString()).put(AUTHOR_FULL.getFirstName()),
                    new AttributeUpdate(LAST_NAME.toString()).put(AUTHOR_FULL.getLastName()),
                    new AttributeUpdate(DATA_VERSION.toString()).addNumeric(1),
                    new AttributeUpdate(EMAIL.toString()).put(AUTHOR_FULL.getEmail().orElseThrow().value()))
            .withExpected(new Expected(DATA_VERSION.toString()).eq(AUTHOR_FULL.getDataVersion()));

    @Test
    public void GIVEN_Author_with_non_optional_values_WHEN_calling_toPutItemSpec_THEN_return_expected_PutItemSpec() {
        final PutItemSpec actual = AuthorToItemSpecConverter.toPutItemSpec(AUTHOR_NON_OPT);
        assertEquals(actual.getItem(), PUT_ITEM_SPEC_NON_OPT.getItem());
        assertDdbExpectedCondition(actual.getExpected(), PUT_ITEM_SPEC_NON_OPT.getExpected());
    }

    @Test
    public void GIVEN_Author_with_full_values_WHEN_calling_toPutItemSpec_THEN_return_expected_PutItemSpec() {
        final PutItemSpec actual = AuthorToItemSpecConverter.toPutItemSpec(AUTHOR_FULL);
        assertEquals(actual.getItem(), PUT_ITEM_SPEC_FULL.getItem());
        assertDdbExpectedCondition(actual.getExpected(), PUT_ITEM_SPEC_FULL.getExpected());
    }

    @Test
    public void GIVEN_Author_non_optional_values_WHEN_calling_toUpdateItemSpec_THEN_return_expected_UpdateItemSpec() {
        final UpdateItemSpec actual = AuthorToItemSpecConverter.toUpdateItemSpec(AUTHOR_NON_OPT);
        assertEquals(actual.getKeyComponents(), PRIMARY_KEY_COMPONENTS_NON_OPT);
        assertDdbAttributeUpdates(actual.getAttributeUpdate(), UPDATE_ITEM_SPEC_NON_OPT.getAttributeUpdate());
        assertDdbExpectedCondition(actual.getExpected(), UPDATE_ITEM_SPEC_NON_OPT.getExpected());
    }

    @Test
    public void GIVEN_Author_full_values_WHEN_calling_toUpdateItemSpec_THEN_return_expected_UpdateItemSpec() {
        final UpdateItemSpec actual = AuthorToItemSpecConverter.toUpdateItemSpec(AUTHOR_FULL);
        assertEquals(actual.getKeyComponents(), PRIMARY_KEY_COMPONENTS_FULL);
        assertDdbAttributeUpdates(actual.getAttributeUpdate(), UPDATE_ITEM_SPEC_FULL.getAttributeUpdate());
        assertDdbExpectedCondition(actual.getExpected(), UPDATE_ITEM_SPEC_FULL.getExpected());
    }
}
