package org.example.library.dao.dynamodb.book;

import com.amazonaws.services.dynamodbv2.document.Table;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.example.library.dao.ResourceDeleter;
import org.example.library.models.BookId;

import static com.google.common.base.Preconditions.checkNotNull;

public class BookDeleter implements ResourceDeleter<BookId> {

    private final Table booksTable;

    @Inject
    public BookDeleter(@Named("BooksTable") final Table booksTable) {
        this.booksTable = checkNotNull(booksTable);
    }

    @Override
    public void delete(final BookId bookId) {
        booksTable.deleteItem(BookAttributes.ID.toString(), bookId.value());
    }
}
