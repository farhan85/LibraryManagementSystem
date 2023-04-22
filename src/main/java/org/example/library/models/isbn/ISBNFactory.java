package org.example.library.models.isbn;

import com.google.common.annotations.VisibleForTesting;

import java.util.Random;
import java.util.function.Supplier;

import static org.example.library.models.isbn.ISBN10.ISBN10_LEN;
import static org.example.library.models.isbn.ISBN13.ISBN13_LEN;

public abstract class ISBNFactory {

    private static final Random RANDOM = new Random();

    public static ISBN10 randomISBN10() {
        return buildISBN10(() -> RANDOM.nextInt(10));
    }

    public static ISBN13 randomISBN13() {
        return buildISBN13(() -> RANDOM.nextInt(10));
    }

    public static ISBN fromString(final String isbnStr) {
        if (isbnStr.length() == ISBN10_LEN) {
            return ImmutableISBN10.of(isbnStr);
        } else if (isbnStr.length() == ISBN13_LEN) {
            return ImmutableISBN13.of(isbnStr);
        } else {
            throw new IllegalArgumentException(String.format(
                    "Invalid ISBN string length. Expected: %d or %d, Actual: %d",
                    ISBN10_LEN, ISBN13_LEN, isbnStr.length()));
        }
    }

    @VisibleForTesting
    static ISBN10 buildISBN10(final Supplier<Integer> rng) {
        final StringBuilder builder = new StringBuilder(ISBN10_LEN);
        for (int i = 0; i < ISBN10_LEN - 1; i++) {
            builder.append(rng.get());
        }
        builder.append(0);
        final String isbnStr = builder.toString();
        final int checksum = ISBN10.checksum(isbnStr);
        final int lastDigit = checksum % 11;
        final String lastChar = lastDigit == 10 ? "X" : String.valueOf(lastDigit);
        return ImmutableISBN10.of(String.format("%s%s",
                isbnStr.substring(0, ISBN10_LEN - 1), lastChar));
    }

    @VisibleForTesting
    static ISBN13 buildISBN13(final Supplier<Integer> rng) {
        final StringBuilder builder = new StringBuilder(ISBN13_LEN);
        for (int i = 0; i < ISBN13_LEN - 1; i++) {
            builder.append(rng.get());
        }
        builder.append(0);
        final String isbnStr = builder.toString();
        final int checksum = ISBN13.checksum(isbnStr);
        final int lastDigit = 10 - (checksum % 10);
        final String lastChar = lastDigit == 10 ? "0" : String.valueOf(lastDigit);
        return ImmutableISBN13.of(String.format("%s%s",
                isbnStr.substring(0, ISBN13_LEN - 1), lastChar));
    }
}
