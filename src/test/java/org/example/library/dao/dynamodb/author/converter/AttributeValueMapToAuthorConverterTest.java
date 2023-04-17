package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.collect.ImmutableMap;
import org.example.library.dao.dynamodb.author.AuthorAttributes;
import org.example.library.models.Author;
import org.example.library.testutils.AuthorFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class AttributeValueMapToAuthorConverterTest {

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

    private AttributeValueMapToAuthorConverter attributeValueMapToAuthorConverter;

    @BeforeMethod
    public void setup() {
        attributeValueMapToAuthorConverter = new AttributeValueMapToAuthorConverter();
    }

    @Test
    public void GIVEN_AttributeValueMap_with_no_optional_Author_values_WHEN_calling_apply_THEN_return_expected_Author() {
        assertEquals(attributeValueMapToAuthorConverter.apply(AUTHOR_MAP_NON_OPT), AUTHOR_NON_OPT);
    }

    @Test
    public void GIVEN_AttributeValueMap_with_full_Author_values_WHEN_calling_apply_THEN_return_expected_Author() {
        assertEquals(attributeValueMapToAuthorConverter.apply(AUTHOR_MAP_FULL), AUTHOR_FULL);
    }
}
