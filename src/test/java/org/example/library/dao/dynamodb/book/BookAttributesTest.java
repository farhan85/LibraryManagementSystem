package org.example.library.dao.dynamodb.book;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class BookAttributesTest {

    @Test
    public void GIVEN_AuthorAttributes_enum_WHEN_calling_toString_THEN_return_table_attribute_name() {
        assertEquals(BookAttributes.ID.toString(), "Id");
        assertEquals(BookAttributes.ISBN.toString(), "Isbn");
        assertEquals(BookAttributes.TITLE.toString(), "Title");
        assertEquals(BookAttributes.AUTHOR_ID.toString(), "AuthorId");
        assertEquals(BookAttributes.AUTHOR_FIRST_NAME.toString(), "AuthorFirstName");
        assertEquals(BookAttributes.AUTHOR_LAST_NAME.toString(), "AuthorLastName");
        assertEquals(BookAttributes.GENRE.toString(), "Genre");
        assertEquals(BookAttributes.YEAR.toString(), "Year");
        assertEquals(BookAttributes.COUNT.toString(), "Count");
        assertEquals(BookAttributes.DATA_VERSION.toString(), "DataVersion");
    }
}
