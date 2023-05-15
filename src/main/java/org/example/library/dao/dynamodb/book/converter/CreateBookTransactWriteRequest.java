package org.example.library.dao.dynamodb.book.converter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.TransactWriteItem;
import com.amazonaws.services.dynamodbv2.model.TransactWriteItemsRequest;
import com.amazonaws.services.dynamodbv2.model.TransactionCanceledException;
import com.google.common.base.Converter;
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
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

public class CreateBookTransactWriteRequest implements TransactWriteItemsRequestGenerator<Book> {

    private final String booksTableName;
    private final String authorsTableName;
    private final Converter<Book, Map<String, AttributeValue>> attributeValueMapConverter;
    private final Supplier<TransactWriteItemBuilder> transactWriteItemBuilderSupplier;
    private final Supplier<TransactionFailedMessageBuilder> transactionFailedMessageBuilderSupplier;

    @Inject
    public CreateBookTransactWriteRequest(@Named("BooksTableName") final String booksTableName,
                                          @Named("AuthorsTableName") final String authorsTableName,
                                          final Converter<Book, Map<String, AttributeValue>> attributeValueMapConverter,
                                          final Supplier<TransactWriteItemBuilder> transactWriteItemBuilderSupplier,
                                          final Supplier<TransactionFailedMessageBuilder> transactionFailedMessageBuilderSupplier) {
        this.booksTableName = checkNotNull(booksTableName);
        this.authorsTableName = checkNotNull(authorsTableName);
        this.attributeValueMapConverter = checkNotNull(attributeValueMapConverter);
        this.transactWriteItemBuilderSupplier = checkNotNull(transactWriteItemBuilderSupplier);
        this.transactionFailedMessageBuilderSupplier = checkNotNull(transactionFailedMessageBuilderSupplier);
    }

    @Override
    public TransactWriteItemsRequest createRequest(final Book book) {
        final List<TransactWriteItem> transactWriteItems = ImmutableList.of(
                createBookWriteItem(book),
                checkAuthorExistsWriteItem(book));
        return new TransactWriteItemsRequest()
                .withTransactItems(transactWriteItems);
    }

    @Override
    public String createTransactionCanceledExceptionMessage(final TransactionCanceledException e, final Book book) {
        final UUID bookUuid = book.getId().uuid();
        final UUID authorUuid = book.getAuthor().getId().uuid();
        return transactionFailedMessageBuilderSupplier.get()
                .withMainError("Failed to create Book")
                .withSubError(String.format("Book already exists. BookId=%s", bookUuid))
                .withSubError(String.format("Author does not exist. BookId=%s, AuthorId=%s", bookUuid, authorUuid))
                .withTransactionCanceledException(e)
                .build();
    }

    private TransactWriteItem createBookWriteItem(final Book book) {
        return transactWriteItemBuilderSupplier.get()
                .withTableName(booksTableName)
                .withItems(attributeValueMapConverter.convert(book))
                .conditionNotExists(BookAttributes.ID.toString())
                .buildPutItem();
    }

    private TransactWriteItem checkAuthorExistsWriteItem(final Book book) {
        return transactWriteItemBuilderSupplier.get()
                .withTableName(authorsTableName)
                .withKey(AuthorAttributes.ID.toString(), book.getAuthor().getId().value())
                .conditionExists(AuthorAttributes.ID.toString())
                .buildConditionCheckItem();
    }
}
