package org.example.library.dao.dynamodb.book.converter;

import com.amazonaws.services.dynamodbv2.model.*;
import com.google.common.collect.ImmutableList;
import org.example.library.dao.dynamodb.TransactWriteItemBuilder;
import org.example.library.dao.dynamodb.TransactionFailedMessageBuilder;
import org.example.library.dao.dynamodb.author.AuthorAttributes;
import org.example.library.dao.dynamodb.book.BookAttributes;
import org.example.library.models.AuthorId;
import org.example.library.models.Book;
import org.example.library.testutils.BookFactory;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.function.Supplier;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class CreateBookTransactWriteRequestTest {

    private static final String BOOKS_TABLE_NAME = "books-table";
    private static final String AUTHORS_TABLE_NAME = "authors-table";
    private static final Book BOOK = BookFactory.random();
    private static final AuthorId AUTHOR_ID = BOOK.getAuthor().getId();
    private static final AttributeValueMapToBookConverter ATTRIBUTE_MAP_CONVERTER = new AttributeValueMapToBookConverter();
    private static final Map<String, AttributeValue> BOOK_ATTRIBUTE_VALUES = ATTRIBUTE_MAP_CONVERTER.convert(BOOK);

    @Mock
    private Supplier<TransactWriteItemBuilder> mockTransactWriteItemBuilderSupplier;
    @Mock
    private TransactWriteItemBuilder mockCreateBookItemBuilder;
    @Mock
    private TransactWriteItemBuilder mockCheckAuthorExistsItemBuilder;
    @Mock
    private TransactWriteItem mockCreateBookWriteItem;
    @Mock
    private TransactWriteItem mockCheckAuthorExistsWriteItem;
    @Mock
    private Supplier<TransactionFailedMessageBuilder> mockTransactionFailedMessageBuilderSupplier;
    @Mock
    private TransactionFailedMessageBuilder mockTransactionFailedMessageBuilder;

    private CreateBookTransactWriteRequest createBookTransactWriteRequest;

    @BeforeMethod
    public void setup() {
        createBookTransactWriteRequest = new CreateBookTransactWriteRequest(BOOKS_TABLE_NAME, AUTHORS_TABLE_NAME,
                ATTRIBUTE_MAP_CONVERTER, mockTransactWriteItemBuilderSupplier, mockTransactionFailedMessageBuilderSupplier);
    }

    @Test
    public void GIVEN_Book_WHEN_calling_createRequest_THEN_return_expected_TransactWriteItemsRequest() {
        when(mockTransactWriteItemBuilderSupplier.get())
                .thenReturn(mockCreateBookItemBuilder)
                .thenReturn(mockCheckAuthorExistsItemBuilder);

        when(mockCreateBookItemBuilder.withTableName(BOOKS_TABLE_NAME)).thenReturn(mockCreateBookItemBuilder);
        when(mockCreateBookItemBuilder.withItems(BOOK_ATTRIBUTE_VALUES)).thenReturn(mockCreateBookItemBuilder);
        when(mockCreateBookItemBuilder.conditionNotExists(BookAttributes.ID.toString())).thenReturn(mockCreateBookItemBuilder);
        when(mockCreateBookItemBuilder.buildPutItem()).thenReturn(mockCreateBookWriteItem);

        when(mockCheckAuthorExistsItemBuilder.withTableName(AUTHORS_TABLE_NAME)).thenReturn(mockCheckAuthorExistsItemBuilder);
        when(mockCheckAuthorExistsItemBuilder.withKey(AuthorAttributes.ID.toString(), AUTHOR_ID.value())).thenReturn(mockCheckAuthorExistsItemBuilder);
        when(mockCheckAuthorExistsItemBuilder.conditionExists(AuthorAttributes.ID.toString())).thenReturn(mockCheckAuthorExistsItemBuilder);
        when(mockCheckAuthorExistsItemBuilder.buildConditionCheckItem()).thenReturn(mockCheckAuthorExistsWriteItem);

        final TransactWriteItemsRequest expected = new TransactWriteItemsRequest()
                .withTransactItems(mockCreateBookWriteItem, mockCheckAuthorExistsWriteItem);

        assertEquals(createBookTransactWriteRequest.createRequest(BOOK), expected);
    }

    @Test
    public void GIVEN_TransactionCanceledException_and_Book_WHEN_calling_createTransactionCanceledExceptionMessage_THEN_return_expected_error_message() {
        final TransactionCanceledException exception = new TransactionCanceledException("test-exception")
                .withCancellationReasons(ImmutableList.of(
                        new CancellationReason().withCode("Failed first conditional check"),
                        new CancellationReason().withCode("Failed second conditional check")));

        final String subError1 = String.format("Book already exists. BookId=%s", BOOK.getId().value());
        final String subError2 = String.format("Author does not exist. BookId=%s, AuthorId=%s",
                BOOK.getId().value(), BOOK.getAuthor().getId().value());

        final String errorMessage = "error-message";
        when(mockTransactionFailedMessageBuilderSupplier.get()).thenReturn(mockTransactionFailedMessageBuilder);
        when(mockTransactionFailedMessageBuilder.withMainError("Failed to create Book")).thenReturn(mockTransactionFailedMessageBuilder);
        when(mockTransactionFailedMessageBuilder.withSubError(subError1)).thenReturn(mockTransactionFailedMessageBuilder);
        when(mockTransactionFailedMessageBuilder.withSubError(subError2)).thenReturn(mockTransactionFailedMessageBuilder);
        when(mockTransactionFailedMessageBuilder.withTransactionCanceledException(exception)).thenReturn(mockTransactionFailedMessageBuilder);
        when(mockTransactionFailedMessageBuilder.build()).thenReturn(errorMessage);

        assertEquals(
                createBookTransactWriteRequest.createTransactionCanceledExceptionMessage(exception, BOOK),
                errorMessage);
    }
}
