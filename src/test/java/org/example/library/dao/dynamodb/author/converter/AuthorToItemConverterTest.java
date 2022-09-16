package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.document.Item;
import org.example.library.dao.dynamodb.author.AuthorAttributes;
import org.example.library.models.Author;
import org.example.library.testutils.AuthorFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class AuthorToItemConverterTest {


    private static final Author AUTHOR = AuthorFactory.random();
    private static final Item AUTHOR_ITEM = new Item()
            .withString(AuthorAttributes.ID.toString(), AUTHOR.getId().toString())
            .withString(AuthorAttributes.FIRST_NAME.toString(), AUTHOR.getFirstName())
            .withString(AuthorAttributes.LAST_NAME.toString(), AUTHOR.getLastName())
            .withNumber(AuthorAttributes.DATA_VERSION.toString(), AUTHOR.getDataVersion());

    @Test
    public void GIVEN_Item_with_Author_values_WHEN_calling_toAuthor_THEN_return_expected_Author() {
        assertEquals(AuthorToItemConverter.toAuthor(AUTHOR_ITEM), AUTHOR);
    }

    @Test
    public void GIVEN_Author_WHEN_calling_toItem_THEN_return_expected_Item() {
        assertEquals(AuthorToItemConverter.toItem(AUTHOR), AUTHOR_ITEM);
    }
}
