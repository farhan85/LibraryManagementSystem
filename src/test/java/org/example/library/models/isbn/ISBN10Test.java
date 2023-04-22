package org.example.library.models.isbn;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ISBN10Test {

    @Test
    public void GIVEN_valid_isbn10_value_WHEN_calling_value_THEN_return_isbn_value_as_string() {
        final ISBN10 isbn10 = ImmutableISBN10.of("1436456185");
        assertEquals(isbn10.value(), "1436456185");
    }

    @Test
    public void GIVEN_valid_isbn10_value_with_last_char_X_WHEN_calling_value_THEN_return_isbn_value_as_string() {
        final ISBN10 isbn10 = ImmutableISBN10.of("080701429X");
        assertEquals(isbn10.value(), "080701429X");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void GIVEN_isbn10_value_incorrect_length_WHEN_calling_value_THEN_return_throw_IllegalArgumentException() {
        ImmutableISBN10.of("123456789012345");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void GIVEN_isbn10_value_incorrect_final_digit_WHEN_calling_value_THEN_return_throw_IllegalArgumentException() {
        ImmutableISBN10.of("1912571550");
    }
}
