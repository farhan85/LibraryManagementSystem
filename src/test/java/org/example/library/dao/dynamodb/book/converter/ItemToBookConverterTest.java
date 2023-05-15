package org.example.library.dao.dynamodb.book.converter;

import com.amazonaws.services.dynamodbv2.document.Item;
import org.example.library.dao.dynamodb.book.BookAttributes;
import org.example.library.models.Book;
import org.example.library.testutils.BookFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ItemToBookConverterTest {

    private static final Book BOOK = BookFactory.random();
    private static final Item ITEM = new Item()
            .withString(BookAttributes.ID.toString(), BOOK.getId().value())
            .withString(BookAttributes.ISBN.toString(), BOOK.getIsbn().value())
            .withString(BookAttributes.AUTHOR_ID.toString(), BOOK.getAuthor().getId().value())
            .withString(BookAttributes.AUTHOR_FIRST_NAME.toString(), BOOK.getAuthor().getFirstName())
            .withString(BookAttributes.AUTHOR_LAST_NAME.toString(), BOOK.getAuthor().getLastName())
            .withString(BookAttributes.TITLE.toString(), BOOK.getTitle())
            .withString(BookAttributes.GENRE.toString(), BOOK.getGenre().name())
            .withNumber(BookAttributes.YEAR.toString(), BOOK.getYear())
            .withNumber(BookAttributes.COUNT.toString(), BOOK.getCount())
            .withNumber(BookAttributes.DATA_VERSION.toString(), BOOK.getDataVersion());

    private ItemToBookConverter itemToBookConverter;

    @BeforeMethod
    public void setup() {
        itemToBookConverter = new ItemToBookConverter();
    }

    @Test
    public void GIVEN_Item_WHEN_calling_apply_THEN_return_expected_Book() {
        assertEquals(itemToBookConverter.apply(ITEM), BOOK);
    }
}
