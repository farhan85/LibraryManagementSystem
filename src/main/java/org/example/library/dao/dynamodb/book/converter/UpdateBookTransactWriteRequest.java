package org.example.library.dao.dynamodb.book.converter;

import com.amazonaws.services.dynamodbv2.model.ReturnValuesOnConditionCheckFailure;
import com.amazonaws.services.dynamodbv2.model.TransactWriteItem;
import com.amazonaws.services.dynamodbv2.model.TransactWriteItemsRequest;
import com.amazonaws.services.dynamodbv2.model.TransactionCanceledException;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.example.library.dao.dynamodb.TransactWriteItemBuilder;
import org.example.library.dao.dynamodb.TransactWriteItemsRequestGenerator;
import org.example.library.dao.dynamodb.TransactionFailedMessageBuilder;
import org.example.library.dao.dynamodb.author.AuthorAttributes;
import org.example.library.dao.dynamodb.book.BookAttributes;
import org.example.library.models.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateBookTransactWriteRequest implements TransactWriteItemsRequestGenerator<Book> {

    private final String booksTableName;
    private final String authorsTableName;
    private final Supplier<TransactWriteItemBuilder> transactWriteItemBuilderSupplier;
    private final Supplier<TransactionFailedMessageBuilder> transactionFailedMessageBuilderSupplier;

    @Inject
    public UpdateBookTransactWriteRequest(@Named("BooksTableName") final String booksTableName,
                                          @Named("AuthorsTableName") final String authorsTableName,
                                          final Supplier<TransactWriteItemBuilder> transactWriteItemBuilderSupplier,
                                          final Supplier<TransactionFailedMessageBuilder> transactionFailedMessageBuilderSupplier) {
        this.booksTableName = checkNotNull(booksTableName);
        this.authorsTableName = checkNotNull(authorsTableName);
        this.transactWriteItemBuilderSupplier = checkNotNull(transactWriteItemBuilderSupplier);
        this.transactionFailedMessageBuilderSupplier = checkNotNull(transactionFailedMessageBuilderSupplier);
    }

    @Override
    public TransactWriteItemsRequest createRequest(final Book book) {
        final List<TransactWriteItem> transactWriteItems = ImmutableList.of(
                updateBookWriteItem(book),
                checkAuthorExistsWriteItem(book));
        return new TransactWriteItemsRequest()
                .withTransactItems(transactWriteItems);
    }

    @Override
    public String createTransactionCanceledExceptionMessage(final TransactionCanceledException e, final Book book) {
        final UUID bookUuid = book.getId().uuid();
        final UUID authorUuid = book.getAuthor().getId().uuid();
        final int expectedDataVersion = book.getDataVersion();
        final String actualDataVersion = Optional.ofNullable(e.getCancellationReasons().get(0).getItem())
                .map(item -> item.get(BookAttributes.DATA_VERSION.toString()).getN())
                .orElse("-");
        return transactionFailedMessageBuilderSupplier.get()
                .withMainError("Failed to update Book")
                .withSubError(String.format("Invalid DataVersion found. BookId=%s, ExpectedDataVersion=%d, ActualDataVersion=%s",
                        bookUuid, expectedDataVersion, actualDataVersion))
                .withSubError(String.format("Author does not exist. BookId=%s, AuthorId=%s", bookUuid, authorUuid))
                .withTransactionCanceledException(e)
                .build();
    }

    private TransactWriteItem updateBookWriteItem(final Book book) {
        return transactWriteItemBuilderSupplier.get()
                .withTableName(booksTableName)
                .withKey(BookAttributes.ID.toString(), book.getId().value())
                .setValue(BookAttributes.ISBN.toString(), book.getIsbn().value())
                .setValue(BookAttributes.TITLE.toString(), book.getTitle())
                .setValue(BookAttributes.AUTHOR_ID.toString(), book.getAuthor().getId().value())
                .setValue(BookAttributes.AUTHOR_FIRST_NAME.toString(), book.getAuthor().getFirstName())
                .setValue(BookAttributes.AUTHOR_LAST_NAME.toString(), book.getAuthor().getLastName())
                .setValue(BookAttributes.YEAR.toString(), book.getYear())
                .setValue(BookAttributes.GENRE.toString(), book.getGenre().toString())
                .setValue(BookAttributes.COUNT.toString(), book.getCount())
                .incValue(BookAttributes.DATA_VERSION.toString())
                .conditionEquals(BookAttributes.DATA_VERSION.toString(), book.getDataVersion())
                .withReturnValuesOnConditionCheckFailure(ReturnValuesOnConditionCheckFailure.ALL_OLD)
                .buildUpdateItem();
    }

    private TransactWriteItem checkAuthorExistsWriteItem(final Book book) {
        return transactWriteItemBuilderSupplier.get()
                .withTableName(authorsTableName)
                .withKey(AuthorAttributes.ID.toString(), book.getAuthor().getId().value())
                .conditionExists(AuthorAttributes.ID.toString())
                .buildConditionCheckItem();
    }
}
