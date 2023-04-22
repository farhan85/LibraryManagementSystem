package org.example.library.models.isbn;

import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.function.Supplier;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class ISBNFactoryTest {

    @Mock
    private Supplier<Integer> mockRng;

    @Test
    public void GIVEN_ISBNFactory_WHEN_calling_randomISBN10_THEN_return_valid_isbn10() {
        final ISBN10 isbn10 = ISBNFactory.randomISBN10();
        final int checksum = ISBN10.checksum(isbn10.value());
        assertEquals(checksum % 11, 0);
    }

    @Test
    public void GIVEN_ISBNFactory_WHEN_calling_randomISBN13_THEN_return_valid_isbn13() {
        final ISBN13 isbn13 = ISBNFactory.randomISBN13();
        final int checksum = ISBN13.checksum(isbn13.value());
        assertEquals(checksum % 10, 0);
    }

    @Test
    public void GIVEN_isbn10_string_WHEN_calling_fromString_THEN_return_valid_isbn10() {
        final String isbnStr = "3234032904";
        final ISBN10 expected = ImmutableISBN10.of(isbnStr);
        assertEquals(ISBNFactory.fromString(isbnStr), expected);
    }

    @Test
    public void GIVEN_isbn13_string_WHEN_calling_fromString_THEN_return_valid_isbn10() {
        final String isbnStr = "5500444129657";
        final ISBN13 expected = ImmutableISBN13.of(isbnStr);
        assertEquals(ISBNFactory.fromString(isbnStr), expected);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void GIVEN_isbn_string_invalid_length_WHEN_calling_fromString_THEN_throw_IllegalArgumentException() {
        ISBNFactory.fromString("1234");
    }

    @Test
    public void GIVEN_random_numbers_WHEN_calling_buildISBN10_THEN_return_valid_isbn10() {
        when(mockRng.get())
                .thenReturn(1)
                .thenReturn(2)
                .thenReturn(3)
                .thenReturn(4)
                .thenReturn(5)
                .thenReturn(9)
                .thenReturn(9)
                .thenReturn(9)
                .thenReturn(9);
        final ISBN isbn = ISBNFactory.buildISBN10(mockRng);
        assertEquals(isbn, ImmutableISBN10.of("1234599996"));
    }

    @Test
    public void GIVEN_random_numbers_causing_last_char_X_WHEN_calling_buildISBN10_THEN_return_valid_isbn10() {
        when(mockRng.get())
                .thenReturn(1)
                .thenReturn(2)
                .thenReturn(3)
                .thenReturn(4)
                .thenReturn(5)
                .thenReturn(6)
                .thenReturn(7)
                .thenReturn(8)
                .thenReturn(9);
        final ISBN isbn = ISBNFactory.buildISBN10(mockRng);
        assertEquals(isbn, ImmutableISBN10.of("123456789X"));
    }

    @Test
    public void GIVEN_random_numbers_WHEN_calling_buildISBN13_THEN_return_valid_isbn13() {
        when(mockRng.get())
                .thenReturn(1)
                .thenReturn(2)
                .thenReturn(3)
                .thenReturn(4)
                .thenReturn(5)
                .thenReturn(6)
                .thenReturn(7)
                .thenReturn(8)
                .thenReturn(9)
                .thenReturn(0)
                .thenReturn(1)
                .thenReturn(2);
        final ISBN isbn = ISBNFactory.buildISBN13(mockRng);
        assertEquals(isbn, ImmutableISBN13.of("1234567890128"));
    }

    @Test
    public void GIVEN_random_numbers_causing_last_digit_zero_WHEN_calling_buildISBN13_THEN_return_valid_isbn13() {
        when(mockRng.get())
                .thenReturn(1)
                .thenReturn(2)
                .thenReturn(1)
                .thenReturn(4)
                .thenReturn(5)
                .thenReturn(6)
                .thenReturn(7)
                .thenReturn(8)
                .thenReturn(9)
                .thenReturn(0)
                .thenReturn(1)
                .thenReturn(2);
        final ISBN isbn = ISBNFactory.buildISBN13(mockRng);
        assertEquals(isbn, ImmutableISBN13.of("1214567890120"));
    }
}
