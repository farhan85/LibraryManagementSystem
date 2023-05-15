package org.example.library.dao.dynamodb.book.converter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.base.Converter;
import com.google.common.collect.ImmutableMap;
import org.example.library.dao.dynamodb.book.BookAttributes;
import org.example.library.models.Book;
import org.example.library.models.Genre;
import org.example.library.models.ImmutableAuthorSummary;
import org.example.library.models.ImmutableBook;
import org.example.library.models.isbn.ISBNFactory;

import java.util.Map;
import java.util.UUID;

public class AttributeValueMapToBookConverter extends Converter<Book, Map<String, AttributeValue>> {

    @Override
    protected Map<String, AttributeValue> doForward(final Book book) {
        return ImmutableMap.of(
                BookAttributes.ID.toString(), new AttributeValue().withS(book.getId().value()),
                BookAttributes.ISBN.toString(), new AttributeValue().withS(book.getIsbn().value()),
                BookAttributes.TITLE.toString(), new AttributeValue().withS(book.getTitle()),
                BookAttributes.AUTHOR_ID.toString(), new AttributeValue().withS(book.getAuthor().getId().value()),
                BookAttributes.AUTHOR_FIRST_NAME.toString(), new AttributeValue().withS(book.getAuthor().getFirstName()),
                BookAttributes.AUTHOR_LAST_NAME.toString(), new AttributeValue().withS(book.getAuthor().getLastName()),
                BookAttributes.YEAR.toString(), new AttributeValue().withN(Integer.toString(book.getYear())),
                BookAttributes.GENRE.toString(), new AttributeValue().withS(book.getGenre().toString()),
                BookAttributes.COUNT.toString(), new AttributeValue().withN(Integer.toString(book.getCount())),
                BookAttributes.DATA_VERSION.toString(), new AttributeValue().withN(Integer.toString(book.getDataVersion())));
    }

    @Override
    protected Book doBackward(final Map<String, AttributeValue> item) {
        return ImmutableBook.builder()
                .withId(UUID.fromString(item.get(BookAttributes.ID.toString()).getS()))
                .withIsbn(ISBNFactory.fromString(item.get(BookAttributes.ISBN.toString()).getS()))
                .withAuthor(ImmutableAuthorSummary.builder()
                        .withId(UUID.fromString(item.get(BookAttributes.AUTHOR_ID.toString()).getS()))
                        .withFirstName(item.get(BookAttributes.AUTHOR_FIRST_NAME.toString()).getS())
                        .withLastName(item.get(BookAttributes.AUTHOR_LAST_NAME.toString()).getS())
                        .build())
                .withTitle(item.get(BookAttributes.TITLE.toString()).getS())
                .withGenre(Genre.fromString(item.get(BookAttributes.GENRE.toString()).getS()))
                .withYear(Integer.parseInt(item.get(BookAttributes.YEAR.toString()).getN()))
                .withCount(Integer.parseInt(item.get(BookAttributes.COUNT.toString()).getN()))
                .withDataVersion(Integer.parseInt(item.get(BookAttributes.DATA_VERSION.toString()).getN()))
                .build();
    }
}
