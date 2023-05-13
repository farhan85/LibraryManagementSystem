package org.example.library.models;

import org.example.library.models.isbn.ISBNFactory;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.AssertJUnit.assertEquals;

public class BookTest {

    @Test
    public void GIVEN_no_dataVersion_given_WHEN_creating_Book_THEN_return_Book_with_dataVersion_one() {
        final Book book = ImmutableBook.builder()
                .withId(ImmutableBookId.of(UUID.randomUUID()))
                .withIsbn(ISBNFactory.randomISBN13())
                .withAuthor(ImmutableAuthorSummary.builder()
                        .withId(UUID.randomUUID())
                        .withFirstName("first-name")
                        .withLastName("last-name")
                        .build())
                .withTitle("book-title")
                .withGenre(Genre.FANTASY)
                .withYear(2023)
                .withCount(10)
                .build();
        assertEquals(book.getDataVersion(), 1);
    }
}
