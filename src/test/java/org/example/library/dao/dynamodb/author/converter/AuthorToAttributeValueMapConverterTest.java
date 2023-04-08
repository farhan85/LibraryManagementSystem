package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.collect.ImmutableMap;
import org.example.library.dao.dynamodb.author.AuthorAttributes;
import org.example.library.models.Author;
import org.example.library.testutils.AuthorFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class AuthorToAttributeValueMapConverterTest {

    private static final Author AUTHOR = AuthorFactory.random();
    private static final ImmutableMap<String, AttributeValue> AUTHOR_MAP = ImmutableMap.of(
            AuthorAttributes.ID.toString(), new AttributeValue().withS(AUTHOR.getId().value()),
            AuthorAttributes.FIRST_NAME.toString(), new AttributeValue().withS(AUTHOR.getFirstName()),
            AuthorAttributes.LAST_NAME.toString(), new AttributeValue().withS(AUTHOR.getLastName()),
            AuthorAttributes.DATA_VERSION.toString(), new AttributeValue().withN(Integer.toString(AUTHOR.getDataVersion())));

    @Test
    public void GIVEN_AttributeValueMap_with_Author_values_WHEN_calling_toAuthor_THEN_return_expected_Author() {
        assertEquals(AuthorToAttributeValueMapConverter.toAuthor(AUTHOR_MAP), AUTHOR);
    }

    @Test
    public void GIVEN_Author_WHEN_calling_toAttributeValueMap_THEN_return_expected_AttributeValue() {
        assertEquals(AuthorToAttributeValueMapConverter.toAttributeValueMap(AUTHOR), AUTHOR_MAP);
    }
}
