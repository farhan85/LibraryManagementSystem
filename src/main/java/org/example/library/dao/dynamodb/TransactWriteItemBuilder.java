package org.example.library.dao.dynamodb;

import com.amazonaws.services.dynamodbv2.model.*;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class TransactWriteItemBuilder {

    private String tableName;
    private final Map<String, AttributeValue> keyMap;
    private final Map<String, AttributeValue> items;
    private final List<String> setExpressions;
    private final List<String> conditions;
    private int attributeNameIndex;
    private final Map<String, String> attributeNameMap;
    private int attributeValueIndex;
    private final Map<String, AttributeValue> attributeValueMap;
    private ReturnValuesOnConditionCheckFailure returnValuesOnConditionCheckFailure;

    public TransactWriteItemBuilder() {
        keyMap = new HashMap<>();
        items = new HashMap<>();
        setExpressions = new ArrayList<>();
        conditions = new ArrayList<>();
        attributeNameMap = new HashMap<>();
        attributeValueMap = new HashMap<>();
        returnValuesOnConditionCheckFailure = null;
    }

    public TransactWriteItemBuilder withTableName(final String tableName) {
        this.tableName = checkNotNull(tableName);
        return this;
    }

    public TransactWriteItemBuilder withKey(final String attributeName, final String attributeValue) {
        checkState(keyMap.size() < 2, "Cannot have more than two keys");
        checkNotNull(attributeName);
        checkNotNull(attributeValue);
        keyMap.put(attributeName, new AttributeValue().withS(attributeValue));
        return this;
    }

    public TransactWriteItemBuilder withItems(final Map<String, AttributeValue> items) {
        this.items.putAll(checkNotNull(items));
        return this;
    }

    public TransactWriteItemBuilder setValue(final String attributeName, final int value) {
        checkNotNull(attributeName);
        setValue(attributeName, new AttributeValue().withN(Integer.toString(value)));
        return this;
    }

    public TransactWriteItemBuilder setValue(final String attributeName, final String value) {
        checkNotNull(attributeName);
        checkNotNull(value);
        setValue(attributeName, new AttributeValue().withS(value));
        return this;
    }

    private TransactWriteItemBuilder setValue(final String attributeName, final AttributeValue value) {
        final String nameKey = String.format("#n%d", ++attributeNameIndex);
        final String valueKey = String.format(":v%d", ++attributeValueIndex);
        setExpressions.add(String.format("%s = %s", nameKey, valueKey));
        attributeNameMap.put(nameKey, attributeName);
        attributeValueMap.put(valueKey, value);
        return this;
    }

    public TransactWriteItemBuilder incValue(final String attributeName) {
        checkNotNull(attributeName);
        final String nameKey = String.format("#n%d", ++attributeNameIndex);
        final String incKey = ":inc";
        setExpressions.add(String.format("%s = %s + %s", nameKey, nameKey, incKey));
        attributeNameMap.put(nameKey, attributeName);
        attributeValueMap.put(incKey, new AttributeValue().withN("1"));
        return this;
    }

    public TransactWriteItemBuilder conditionEquals(final String attributeName, final int value) {
        checkNotNull(attributeName);
        final String nameKey = String.format("#n%d", ++attributeNameIndex);
        final String valueKey = String.format(":v%d", ++attributeValueIndex);
        conditions.add(String.format("%s = %s", nameKey, valueKey));
        attributeNameMap.put(nameKey, attributeName);
        attributeValueMap.put(valueKey, new AttributeValue().withN(Integer.toString(value)));
        return this;
    }

    public TransactWriteItemBuilder conditionExists(final String attributeName) {
        checkNotNull(attributeName);
        final String nameKey = String.format("#n%d", ++attributeNameIndex);
        conditions.add(String.format("attribute_exists(%s)", nameKey));
        attributeNameMap.put(nameKey, attributeName);
        return this;
    }

    public TransactWriteItemBuilder conditionNotExists(final String attributeName) {
        checkNotNull(attributeName);
        final String nameKey = String.format("#n%d", ++attributeNameIndex);
        conditions.add(String.format("attribute_not_exists(%s)", nameKey));
        attributeNameMap.put(nameKey, attributeName);
        return this;
    }

    public TransactWriteItemBuilder withReturnValuesOnConditionCheckFailure(final ReturnValuesOnConditionCheckFailure returnValues) {
        this.returnValuesOnConditionCheckFailure = checkNotNull(returnValues);
        return this;
    }

    public TransactWriteItem buildPutItem() {
        final Put put = new Put()
                .withTableName(tableName)
                .withItem(items);
        if (!conditions.isEmpty()) {
            put.withConditionExpression(String.join(", ", conditions));
        }
        if (!attributeNameMap.isEmpty()) {
            put.withExpressionAttributeNames(attributeNameMap);
        }
        if (!attributeValueMap.isEmpty()) {
            put.withExpressionAttributeValues(attributeValueMap);
        }
        Optional.ofNullable(returnValuesOnConditionCheckFailure)
                .ifPresent(put::withReturnValuesOnConditionCheckFailure);
        return new TransactWriteItem().withPut(put);
    }

    public TransactWriteItem buildUpdateItem() {
        final Update update = new Update()
                .withTableName(tableName)
                .withKey(keyMap)
                .withUpdateExpression("SET " + String.join(", ", setExpressions))
                .withExpressionAttributeNames(attributeNameMap)
                .withExpressionAttributeValues(attributeValueMap);
        if (!conditions.isEmpty()) {
            update.withConditionExpression(String.join(", ", conditions));
        }
        Optional.ofNullable(returnValuesOnConditionCheckFailure)
                .ifPresent(update::withReturnValuesOnConditionCheckFailure);
        return new TransactWriteItem().withUpdate(update);
    }

    public TransactWriteItem buildConditionCheckItem() {
        final ConditionCheck conditionCheck = new ConditionCheck()
                .withTableName(tableName)
                .withKey(keyMap)
                .withConditionExpression(String.join(", ", conditions))
                .withExpressionAttributeNames(attributeNameMap);
        if (!attributeValueMap.isEmpty()) {
            conditionCheck.withExpressionAttributeValues(attributeValueMap);
        }
        Optional.ofNullable(returnValuesOnConditionCheckFailure)
                .ifPresent(conditionCheck::withReturnValuesOnConditionCheckFailure);
        return new TransactWriteItem().withConditionCheck(conditionCheck);
    }
}
