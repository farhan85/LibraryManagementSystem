package org.example.library.dao.dynamodb.book.converter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.collect.ImmutableMap;
import org.example.library.dao.dynamodb.book.BookAttributes;
import org.example.library.models.Book;
import org.example.library.testutils.BookFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class AttributeValueMapToBookConverterTest {

    private static final Book BOOK = BookFactory.random();
    private static final ImmutableMap<String, AttributeValue> BOOK_ATTRIBUTE_MAP = ImmutableMap.of(
            BookAttributes.ID.toString(), new AttributeValue().withS(BOOK.getId().value()),
            BookAttributes.ISBN.toString(), new AttributeValue().withS(BOOK.getIsbn().value()),
            BookAttributes.TITLE.toString(), new AttributeValue().withS(BOOK.getTitle()),
            BookAttributes.AUTHOR_ID.toString(), new AttributeValue().withS(BOOK.getAuthor().getId().value()),
            BookAttributes.AUTHOR_FIRST_NAME.toString(), new AttributeValue().withS(BOOK.getAuthor().getFirstName()),
            BookAttributes.AUTHOR_LAST_NAME.toString(), new AttributeValue().withS(BOOK.getAuthor().getLastName()),
            BookAttributes.YEAR.toString(), new AttributeValue().withN(Integer.toString(BOOK.getYear())),
            BookAttributes.GENRE.toString(), new AttributeValue().withS(BOOK.getGenre().toString()),
            BookAttributes.COUNT.toString(), new AttributeValue().withN(Integer.toString(BOOK.getCount())),
            BookAttributes.DATA_VERSION.toString(), new AttributeValue().withN(Integer.toString(BOOK.getDataVersion())));

    private AttributeValueMapToBookConverter attributeValueMapToBookConverter;

    @BeforeMethod
    public void setup() {
        attributeValueMapToBookConverter = new AttributeValueMapToBookConverter();
    }

    @Test
    public void GIVEN_AttributeValueMap_WHEN_calling_doForward_THEN_return_expected_Book() {
        assertEquals(attributeValueMapToBookConverter.doForward(BOOK), BOOK_ATTRIBUTE_MAP);
    }

    @Test
    public void GIVEN_Book_WHEN_calling_doBackward_THEN_return_expected_AttributeValueMap() {
        assertEquals(attributeValueMapToBookConverter.doBackward(BOOK_ATTRIBUTE_MAP), BOOK);
    }
}
