package org.example.library.dao.dynamodb.book;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.TransactionCanceledException;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.example.library.dao.ResourceCreator;
import org.example.library.dao.dynamodb.TransactWriteItemsRequestGenerator;
import org.example.library.models.Book;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class BookCreator implements ResourceCreator<Book> {

    private final AmazonDynamoDB dynamoDB;
    private final TransactWriteItemsRequestGenerator<Book> transactRequestGenerator;

    @Inject
    public BookCreator(final AmazonDynamoDB dynamoDB,
                       @Named("CreateBookTransactGenerator") final TransactWriteItemsRequestGenerator<Book> transactRequestGenerator) {
        this.dynamoDB = checkNotNull(dynamoDB);
        this.transactRequestGenerator = checkNotNull(transactRequestGenerator);
    }

    @Override
    public void create(final Book book) {
        checkArgument(book.getDataVersion() == 1);
        try {
            dynamoDB.transactWriteItems(transactRequestGenerator.createRequest(book));
        } catch (final TransactionCanceledException e) {
            throw new RuntimeException(transactRequestGenerator.createTransactionCanceledExceptionMessage(e, book), e);
        }
    }
}
