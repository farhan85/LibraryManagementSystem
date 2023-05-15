package org.example.library.dao.dynamodb.book.converter;

import com.amazonaws.services.dynamodbv2.document.Item;
import org.example.library.dao.dynamodb.book.BookAttributes;
import org.example.library.models.Book;
import org.example.library.models.Genre;
import org.example.library.models.ImmutableAuthorSummary;
import org.example.library.models.ImmutableBook;
import org.example.library.models.isbn.ISBNFactory;

import java.util.UUID;
import java.util.function.Function;

public class ItemToBookConverter implements Function<Item, Book> {

    @Override
    public Book apply(final Item item) {
        return ImmutableBook.builder()
                .withId(UUID.fromString(item.getString(BookAttributes.ID.toString())))
                .withIsbn(ISBNFactory.fromString(item.getString(BookAttributes.ISBN.toString())))
                .withAuthor(ImmutableAuthorSummary.builder()
                        .withId(UUID.fromString(item.getString(BookAttributes.AUTHOR_ID.toString())))
                        .withFirstName(item.getString(BookAttributes.AUTHOR_FIRST_NAME.toString()))
                        .withLastName(item.getString(BookAttributes.AUTHOR_LAST_NAME.toString()))
                        .build())
                .withTitle(item.getString(BookAttributes.TITLE.toString()))
                .withGenre(Genre.fromString(item.getString(BookAttributes.GENRE.toString())))
                .withYear(item.getInt(BookAttributes.YEAR.toString()))
                .withCount(item.getInt(BookAttributes.COUNT.toString()))
                .withDataVersion(item.getInt(BookAttributes.DATA_VERSION.toString()))
                .build();
    }
}
