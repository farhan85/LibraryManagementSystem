package org.example.library.testutils;

import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.Expected;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static org.testng.Assert.assertEquals;

/**
 * Assert methods for DDB objects which have not implemented the equals/hash methods.
 */
public class AssertDdbObjects {

    public static void assertDdbAttributeUpdates(final List<AttributeUpdate> actualList,
                                                 final List<AttributeUpdate> expectedList) {
        assertEquals(actualList.size(), expectedList.size());
        IntStream.range(0, expectedList.size())
                .forEach(i -> {
                    final AttributeUpdate actual = actualList.get(i);
                    final AttributeUpdate expected = expectedList.get(i);
                    assertEquals(actual.getAttributeName(), expected.getAttributeName());
                    assertEquals(actual.getValue(), expected.getValue());
                });
    }

    public static void assertDdbExpectedCondition(final Collection<Expected> actualCol,
                                                  final Collection<Expected> expectedCol) {
        assertEquals(actualCol.size(), expectedCol.size());
        final Iterator<Expected> actualColIterator = actualCol.iterator();
        final Iterator<Expected> expectedColIterator = expectedCol.iterator();
        while(actualColIterator.hasNext() && expectedColIterator.hasNext()) {
            final Expected actual = actualColIterator.next();
            final Expected expected = expectedColIterator.next();
            assertEquals(actual.getAttribute(), expected.getAttribute());
            assertEquals(actual.getComparisonOperator(), expected.getComparisonOperator());
            assertEquals(actual.getValues(), expected.getValues());
        }
    }
}
