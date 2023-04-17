package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.document.Item;
import org.example.library.models.Author;
import org.example.library.testutils.AuthorFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.example.library.dao.dynamodb.author.AuthorAttributes.DATA_VERSION;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.EMAIL;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.FIRST_NAME;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.ID;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.LAST_NAME;
import static org.testng.Assert.assertEquals;

public class ItemToAuthorConverterTest {

    private static final Author AUTHOR_NON_OPT = AuthorFactory.randomNonOptionals();
    private static final Author AUTHOR_FULL = AuthorFactory.random();
    private static final Item AUTHOR_NON_OPT_ITEM = new Item()
            .withString(ID.toString(), AUTHOR_NON_OPT.getId().value())
            .withString(FIRST_NAME.toString(), AUTHOR_NON_OPT.getFirstName())
            .withString(LAST_NAME.toString(), AUTHOR_NON_OPT.getLastName())
            .withNumber(DATA_VERSION.toString(), AUTHOR_NON_OPT.getDataVersion());
    private static final Item AUTHOR_FULL_ITEM = new Item()
            .withString(ID.toString(), AUTHOR_FULL.getId().value())
            .withString(FIRST_NAME.toString(), AUTHOR_FULL.getFirstName())
            .withString(LAST_NAME.toString(), AUTHOR_FULL.getLastName())
            .withNumber(DATA_VERSION.toString(), AUTHOR_FULL.getDataVersion())
            .withString(EMAIL.toString(), AUTHOR_FULL.getEmail().orElseThrow().value());

    private ItemToAuthorConverter itemToAuthorConverter;

    @BeforeMethod
    public void setup() {
        itemToAuthorConverter = new ItemToAuthorConverter();
    }

    @Test
    public void GIVEN_Item_with_non_optional_Author_values_WHEN_calling_toAuthor_THEN_return_expected_Author() {
        assertEquals(itemToAuthorConverter.apply(AUTHOR_NON_OPT_ITEM), AUTHOR_NON_OPT);
    }

    @Test
    public void GIVEN_Item_with_full_Author_values_WHEN_calling_toAuthor_THEN_return_expected_Author() {
        assertEquals(itemToAuthorConverter.apply(AUTHOR_FULL_ITEM), AUTHOR_FULL);
    }
}
