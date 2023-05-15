package org.example.library.dao.dynamodb.book;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.example.library.dao.ResourceRetriever;
import org.example.library.models.Book;
import org.example.library.models.BookId;

import java.util.Optional;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

public class BookRetriever implements ResourceRetriever<BookId, Book> {

    private final Table booksTable;
    private final Function<Item, Book> itemToBookConverter;

    @Inject
    public BookRetriever(@Named("BooksTable") final Table booksTable,
                         final Function<Item, Book> itemToBookConverter) {
        this.booksTable = checkNotNull(booksTable);
        this.itemToBookConverter = checkNotNull(itemToBookConverter);
    }

    @Override
    public Optional<Book> get(final BookId bookId) {
        final Item item = booksTable.getItem(BookAttributes.ID.toString(), bookId.value());
        return Optional.ofNullable(item)
                .map(itemToBookConverter);
    }
}
