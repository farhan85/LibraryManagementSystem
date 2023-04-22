package org.example.library.models.isbn;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ISBN13Test {

    @Test
    public void GIVEN_valid_isbn13_value_WHEN_calling_value_THEN_return_isbn_value_as_string() {
        final ISBN13 isbn13 = ImmutableISBN13.of("9781861972712");
        assertEquals(isbn13.value(), "9781861972712");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void GIVEN_isbn13_value_incorrect_length_WHEN_calling_value_THEN_return_throw_IllegalArgumentException() {
        ImmutableISBN13.of("123456789012345");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void GIVEN_isbn13_value_incorrect_final_digit_WHEN_calling_value_THEN_return_throw_IllegalArgumentException() {
        ImmutableISBN13.of("1234567890123");
    }
}
