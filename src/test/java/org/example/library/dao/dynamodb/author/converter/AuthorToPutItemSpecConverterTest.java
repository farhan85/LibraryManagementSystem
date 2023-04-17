package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.document.Expected;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import org.example.library.models.Author;
import org.example.library.testutils.AuthorFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.example.library.dao.dynamodb.author.AuthorAttributes.DATA_VERSION;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.EMAIL;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.FIRST_NAME;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.ID;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.LAST_NAME;
import static org.example.library.testutils.AssertDdbObjects.assertDdbExpectedCondition;
import static org.testng.Assert.assertEquals;

public class AuthorToPutItemSpecConverterTest {

    private static final Author AUTHOR_NON_OPT = AuthorFactory.randomNonOptionals();
    private static final Author AUTHOR_FULL = AuthorFactory.random();
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

    private AuthorToPutItemSpecConverter authorToPutItemSpecConverter;

    @BeforeMethod
    public void setup() {
        authorToPutItemSpecConverter = new AuthorToPutItemSpecConverter();
    }

    @Test
    public void GIVEN_Author_with_non_optional_values_WHEN_calling_apply_THEN_return_expected_PutItemSpec() {
        final PutItemSpec actual = authorToPutItemSpecConverter.apply(AUTHOR_NON_OPT);
        assertEquals(actual.getItem(), PUT_ITEM_SPEC_NON_OPT.getItem());
        assertDdbExpectedCondition(actual.getExpected(), PUT_ITEM_SPEC_NON_OPT.getExpected());
    }

    @Test
    public void GIVEN_Author_with_full_values_WHEN_calling_apply_THEN_return_expected_PutItemSpec() {
        final PutItemSpec actual = authorToPutItemSpecConverter.apply(AUTHOR_FULL);
        assertEquals(actual.getItem(), PUT_ITEM_SPEC_FULL.getItem());
        assertDdbExpectedCondition(actual.getExpected(), PUT_ITEM_SPEC_FULL.getExpected());
    }
}
