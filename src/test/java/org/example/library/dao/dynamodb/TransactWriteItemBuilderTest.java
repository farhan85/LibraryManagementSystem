package org.example.library.dao.dynamodb;

import com.amazonaws.services.dynamodbv2.model.*;
import com.google.common.collect.ImmutableMap;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;

public class TransactWriteItemBuilderTest {

    private TransactWriteItemBuilder transactWriteItemBuilder;

    @BeforeMethod
    public void setup() {
        transactWriteItemBuilder = new TransactWriteItemBuilder();
    }

    @Test
    public void GIVEN_all_values_for_put_WHEN_calling_buildPutItem_THEN_return_expected_TransactWriteItem() {
        final Map<String, AttributeValue> items = ImmutableMap.of(
                "attr1", new AttributeValue().withS("value1"),
                "attr2", new AttributeValue().withS("value2"),
                "attr3", new AttributeValue().withN("10"));
        final Put expectedPut = new Put()
                .withTableName("tableName")
                .withItem(items)
                .withConditionExpression("#n1 = :v1, attribute_exists(#n2), attribute_not_exists(#n3)")
                .withExpressionAttributeNames(ImmutableMap.of(
                        "#n1", "condAttr1",
                        "#n2", "condAttr2",
                        "#n3", "condAttr3"))
                .withExpressionAttributeValues(ImmutableMap.of(
                        ":v1", new AttributeValue().withN("20")))
                .withReturnValuesOnConditionCheckFailure(ReturnValuesOnConditionCheckFailure.ALL_OLD);
        final TransactWriteItem expected = new TransactWriteItem().withPut(expectedPut);

        final TransactWriteItem actual = transactWriteItemBuilder
                .withTableName("tableName")
                .withItems(items)
                .conditionEquals("condAttr1", 20)
                .conditionExists("condAttr2")
                .conditionNotExists("condAttr3")
                .withReturnValuesOnConditionCheckFailure(ReturnValuesOnConditionCheckFailure.ALL_OLD)
                .buildPutItem();

        assertEquals(actual, expected);
    }

    @Test
    public void GIVEN_minimum_values_for_put_WHEN_calling_buildPutItem_THEN_return_expected_TransactWriteItem() {
        final Map<String, AttributeValue> item = ImmutableMap.of("attr1", new AttributeValue().withS("value1"));
        final Put expectedPut = new Put()
                .withTableName("tableName")
                .withItem(item);
        final TransactWriteItem expected = new TransactWriteItem().withPut(expectedPut);

        final TransactWriteItem actual = transactWriteItemBuilder
                .withTableName("tableName")
                .withItems(item)
                .buildPutItem();

        assertEquals(actual, expected);
    }

    @Test
    public void GIVEN_all_values_for_update_WHEN_calling_buildUpdateItem_THEN_return_expected_TransactWriteItem() {
        final Update expectedUpdate = new Update()
                .withTableName("tableName")
                .withKey(ImmutableMap.of("keyAttr", new AttributeValue().withS("keyValue")))
                .withUpdateExpression("SET #n1 = :v1, #n2 = :v2, #n3 = #n3 + :inc")
                .withConditionExpression("#n4 = :v3, attribute_exists(#n5), attribute_not_exists(#n6)")
                .withExpressionAttributeNames(ImmutableMap.of(
                        "#n1", "intAttr",
                        "#n2", "stringAttr",
                        "#n3", "incAttr",
                        "#n4", "condAttr1",
                        "#n5", "condAttr2",
                        "#n6", "condAttr3"))
                .withExpressionAttributeValues(ImmutableMap.of(
                        ":v1", new AttributeValue().withN("10"),
                        ":v2", new AttributeValue().withS("value"),
                        ":inc", new AttributeValue().withN("1"),
                        ":v3", new AttributeValue().withN("20")))
                .withReturnValuesOnConditionCheckFailure(ReturnValuesOnConditionCheckFailure.ALL_OLD);
        final TransactWriteItem expected = new TransactWriteItem().withUpdate(expectedUpdate);

        final TransactWriteItem actual = transactWriteItemBuilder
                .withTableName("tableName")
                .withKey("keyAttr", "keyValue")
                .setValue("intAttr", 10)
                .setValue("stringAttr", "value")
                .incValue("incAttr")
                .conditionEquals("condAttr1", 20)
                .conditionExists("condAttr2")
                .conditionNotExists("condAttr3")
                .withReturnValuesOnConditionCheckFailure(ReturnValuesOnConditionCheckFailure.ALL_OLD)
                .buildUpdateItem();

        assertEquals(actual, expected);
    }

    @Test
    public void GIVEN_minimum_values_for_update_WHEN_calling_buildUpdateItem_THEN_return_expected_TransactWriteItem() {
        final Update expectedUpdate = new Update()
                .withTableName("tableName")
                .withKey(ImmutableMap.of("keyAttr", new AttributeValue().withS("keyValue")))
                .withUpdateExpression("SET #n1 = :v1")
                .withExpressionAttributeNames(ImmutableMap.of("#n1", "attr"))
                .withExpressionAttributeValues(ImmutableMap.of(":v1", new AttributeValue().withS("value")));
        final TransactWriteItem expected = new TransactWriteItem().withUpdate(expectedUpdate);

        final TransactWriteItem actual = transactWriteItemBuilder
                .withTableName("tableName")
                .withKey("keyAttr", "keyValue")
                .setValue("attr", "value")
                .buildUpdateItem();

        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void GIVEN_three_keys_WHEN_calling_withKey_THEN_throw_IllegalStateException() {
        transactWriteItemBuilder
                .withKey("keyAttr1", "keyValue1")
                .withKey("keyAttr2", "keyValue2")
                .withKey("keyAttr3", "keyValue3");
    }

    @Test
    public void GIVEN_all_values_for_conditionCheck_WHEN_calling_buildConditionCheckItem_THEN_return_expected_TransactWriteItem() {
        final ConditionCheck expectedConditionCheck = new ConditionCheck()
                .withTableName("tableName")
                .withKey(ImmutableMap.of("keyAttr", new AttributeValue().withS("keyValue")))
                .withConditionExpression("#n1 = :v1")
                .withExpressionAttributeNames(ImmutableMap.of("#n1", "attr"))
                .withExpressionAttributeValues(ImmutableMap.of(":v1", new AttributeValue().withN("20")))
                .withReturnValuesOnConditionCheckFailure(ReturnValuesOnConditionCheckFailure.ALL_OLD);
        final TransactWriteItem expected = new TransactWriteItem().withConditionCheck(expectedConditionCheck);

        final TransactWriteItem actual = transactWriteItemBuilder
                .withTableName("tableName")
                .withKey("keyAttr", "keyValue")
                .conditionEquals("attr", 20)
                .withReturnValuesOnConditionCheckFailure(ReturnValuesOnConditionCheckFailure.ALL_OLD)
                .buildConditionCheckItem();

        assertEquals(actual, expected);
    }

    @Test
    public void GIVEN_minimum_values_for_conditionCheck_WHEN_calling_buildConditionCheckItem_THEN_return_expected_TransactWriteItem() {
        final ConditionCheck expectedConditionCheck = new ConditionCheck()
                .withTableName("tableName")
                .withKey(ImmutableMap.of("keyAttr", new AttributeValue().withS("keyValue")))
                .withConditionExpression("attribute_exists(#n1)")
                .withExpressionAttributeNames(ImmutableMap.of("#n1", "attr"));
        final TransactWriteItem expected = new TransactWriteItem().withConditionCheck(expectedConditionCheck);

        final TransactWriteItem actual = transactWriteItemBuilder
                .withTableName("tableName")
                .withKey("keyAttr", "keyValue")
                .conditionExists("attr")
                .buildConditionCheckItem();

        assertEquals(actual, expected);
    }
}
