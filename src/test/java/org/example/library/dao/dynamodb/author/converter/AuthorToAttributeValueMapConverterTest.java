package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.collect.ImmutableMap;
import org.example.library.dao.dynamodb.author.AuthorAttributes;
import org.example.library.models.Author;
import org.example.library.testutils.AuthorFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class AuthorToAttributeValueMapConverterTest {

    private static final Author AUTHOR_NON_OPT = AuthorFactory.randomNonOptionals();
    private static final Author AUTHOR_FULL = AuthorFactory.random();
    private static final ImmutableMap<String, AttributeValue> AUTHOR_MAP_NON_OPT = ImmutableMap.of(
            AuthorAttributes.ID.toString(), new AttributeValue().withS(AUTHOR_NON_OPT.getId().value()),
            AuthorAttributes.FIRST_NAME.toString(), new AttributeValue().withS(AUTHOR_NON_OPT.getFirstName()),
            AuthorAttributes.LAST_NAME.toString(), new AttributeValue().withS(AUTHOR_NON_OPT.getLastName()),
            AuthorAttributes.DATA_VERSION.toString(), new AttributeValue().withN(Integer.toString(AUTHOR_NON_OPT.getDataVersion())));
    private static final ImmutableMap<String, AttributeValue> AUTHOR_MAP_FULL = ImmutableMap.of(
            AuthorAttributes.ID.toString(), new AttributeValue().withS(AUTHOR_FULL.getId().value()),
            AuthorAttributes.FIRST_NAME.toString(), new AttributeValue().withS(AUTHOR_FULL.getFirstName()),
            AuthorAttributes.LAST_NAME.toString(), new AttributeValue().withS(AUTHOR_FULL.getLastName()),
            AuthorAttributes.DATA_VERSION.toString(), new AttributeValue().withN(Integer.toString(AUTHOR_FULL.getDataVersion())),
            AuthorAttributes.EMAIL.toString(), new AttributeValue().withS(AUTHOR_FULL.getEmail().orElseThrow().value()));

    @Test
    public void GIVEN_AttributeValueMap_with_no_optional_Author_values_WHEN_calling_toAuthor_THEN_return_expected_Author() {
        assertEquals(AuthorToAttributeValueMapConverter.toAuthor(AUTHOR_MAP_NON_OPT), AUTHOR_NON_OPT);
    }

    @Test
    public void GIVEN_AttributeValueMap_with_full_Author_values_WHEN_calling_toAuthor_THEN_return_expected_Author() {
        assertEquals(AuthorToAttributeValueMapConverter.toAuthor(AUTHOR_MAP_FULL), AUTHOR_FULL);
    }

    @Test
    public void GIVEN_Author_with_no_optional_values_WHEN_calling_toAttributeValueMap_THEN_return_expected_AttributeValue() {
        assertEquals(AuthorToAttributeValueMapConverter.toAttributeValueMap(AUTHOR_NON_OPT), AUTHOR_MAP_NON_OPT);
    }

    @Test
    public void GIVEN_Author_with_full_values_WHEN_calling_toAttributeValueMap_THEN_return_expected_AttributeValue() {
        assertEquals(AuthorToAttributeValueMapConverter.toAttributeValueMap(AUTHOR_FULL), AUTHOR_MAP_FULL);
    }
}
