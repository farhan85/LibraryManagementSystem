package org.example.library.dao.dynamodb.book.converter;

import com.amazonaws.services.dynamodbv2.model.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.example.library.dao.dynamodb.TransactWriteItemBuilder;
import org.example.library.dao.dynamodb.TransactionFailedMessageBuilder;
import org.example.library.dao.dynamodb.author.AuthorAttributes;
import org.example.library.dao.dynamodb.book.BookAttributes;
import org.example.library.models.Book;
import org.example.library.testutils.BookFactory;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.function.Supplier;

import static org.example.library.dao.dynamodb.book.BookAttributes.*;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class UpdateBookTransactWriteRequestTest {

    private static final String BOOKS_TABLE_NAME = "BOOKs-table";
    private static final String AUTHORS_TABLE_NAME = "authors-table";
    private static final Book BOOK = BookFactory.random();

    @Mock
    private Supplier<TransactWriteItemBuilder> mockTransactWriteItemBuilderSupplier;
    @Mock
    private TransactWriteItemBuilder mockUpdateBookItemBuilder;
    @Mock
    private TransactWriteItemBuilder mockCheckAuthorExistsItemBuilder;
    @Mock
    private TransactWriteItem mockUpdateBookWriteItem;
    @Mock
    private TransactWriteItem mockCheckAuthorExistsWriteItem;
    @Mock
    private Supplier<TransactionFailedMessageBuilder> mockTransactionFailedMessageBuilderSupplier;
    @Mock
    private TransactionFailedMessageBuilder mockTransactionFailedMessageBuilder;

    private UpdateBookTransactWriteRequest updateBookTransactWriteRequest;

    @BeforeMethod
    public void setup() {
        updateBookTransactWriteRequest = new UpdateBookTransactWriteRequest(BOOKS_TABLE_NAME, AUTHORS_TABLE_NAME,
                mockTransactWriteItemBuilderSupplier, mockTransactionFailedMessageBuilderSupplier);
    }

    @Test
    public void GIVEN_Book_WHEN_calling_createRequest_THEN_return_expected_TransactWriteItemsRequest() {
        when(mockTransactWriteItemBuilderSupplier.get())
                .thenReturn(mockUpdateBookItemBuilder)
                .thenReturn(mockCheckAuthorExistsItemBuilder);

        when(mockUpdateBookItemBuilder.withTableName(BOOKS_TABLE_NAME)).thenReturn(mockUpdateBookItemBuilder);
        when(mockUpdateBookItemBuilder.withKey(BookAttributes.ID.toString(), BOOK.getId().value())).thenReturn(mockUpdateBookItemBuilder);
        when(mockUpdateBookItemBuilder.setValue(BookAttributes.ISBN.toString(), BOOK.getIsbn().value())).thenReturn(mockUpdateBookItemBuilder);
        when(mockUpdateBookItemBuilder.setValue(BookAttributes.TITLE.toString(), BOOK.getTitle())).thenReturn(mockUpdateBookItemBuilder);
        when(mockUpdateBookItemBuilder.setValue(BookAttributes.AUTHOR_ID.toString(), BOOK.getAuthor().getId().value())).thenReturn(mockUpdateBookItemBuilder);
        when(mockUpdateBookItemBuilder.setValue(BookAttributes.AUTHOR_FIRST_NAME.toString(), BOOK.getAuthor().getFirstName())).thenReturn(mockUpdateBookItemBuilder);
        when(mockUpdateBookItemBuilder.setValue(BookAttributes.AUTHOR_LAST_NAME.toString(), BOOK.getAuthor().getLastName())).thenReturn(mockUpdateBookItemBuilder);
        when(mockUpdateBookItemBuilder.setValue(BookAttributes.YEAR.toString(), BOOK.getYear())).thenReturn(mockUpdateBookItemBuilder);
        when(mockUpdateBookItemBuilder.setValue(BookAttributes.GENRE.toString(), BOOK.getGenre().toString())).thenReturn(mockUpdateBookItemBuilder);
        when(mockUpdateBookItemBuilder.setValue(BookAttributes.COUNT.toString(), BOOK.getCount())).thenReturn(mockUpdateBookItemBuilder);
        when(mockUpdateBookItemBuilder.incValue(BookAttributes.DATA_VERSION.toString())).thenReturn(mockUpdateBookItemBuilder);
        when(mockUpdateBookItemBuilder.conditionEquals(BookAttributes.DATA_VERSION.toString(), BOOK.getDataVersion())).thenReturn(mockUpdateBookItemBuilder);
        when(mockUpdateBookItemBuilder.withReturnValuesOnConditionCheckFailure(ReturnValuesOnConditionCheckFailure.ALL_OLD)).thenReturn(mockUpdateBookItemBuilder);
        when(mockUpdateBookItemBuilder.buildUpdateItem()).thenReturn(mockUpdateBookWriteItem);

        when(mockCheckAuthorExistsItemBuilder.withTableName(AUTHORS_TABLE_NAME)).thenReturn(mockCheckAuthorExistsItemBuilder);
        when(mockCheckAuthorExistsItemBuilder.withKey(AuthorAttributes.ID.toString(), BOOK.getAuthor().getId().value())).thenReturn(mockCheckAuthorExistsItemBuilder);
        when(mockCheckAuthorExistsItemBuilder.conditionExists(AuthorAttributes.ID.toString())).thenReturn(mockCheckAuthorExistsItemBuilder);
        when(mockCheckAuthorExistsItemBuilder.buildConditionCheckItem()).thenReturn(mockCheckAuthorExistsWriteItem);

        final TransactWriteItemsRequest expected = new TransactWriteItemsRequest()
                .withTransactItems(mockUpdateBookWriteItem, mockCheckAuthorExistsWriteItem);

        assertEquals(updateBookTransactWriteRequest.createRequest(BOOK), expected);
    }

    @Test
    public void GIVEN_TransactionCanceledException_and_Book_WHEN_calling_createTransactionCanceledExceptionMessage_THEN_return_expected_error_message() {
        final String differentDataVersion = Integer.toString(BOOK.getDataVersion() + 1);
        final TransactionCanceledException exception = new TransactionCanceledException("test-exception")
                .withCancellationReasons(ImmutableList.of(
                        new CancellationReason()
                                .withCode("Failed first conditional check")
                                .withItem(ImmutableMap.of(DATA_VERSION.toString(), new AttributeValue().withN(differentDataVersion))),
                        new CancellationReason().withCode("Failed second conditional check")));

        final String subError1 = String.format("Invalid DataVersion found. BookId=%s, ExpectedDataVersion=%d, ActualDataVersion=%s",
                BOOK.getId().value(), BOOK.getDataVersion(), differentDataVersion);
        final String subError2 = String.format("Author does not exist. BookId=%s, AuthorId=%s",
                BOOK.getId().value(), BOOK.getAuthor().getId().value());

        final String errorMessage = "error-message";
        when(mockTransactionFailedMessageBuilderSupplier.get()).thenReturn(mockTransactionFailedMessageBuilder);
        when(mockTransactionFailedMessageBuilder.withMainError("Failed to update Book")).thenReturn(mockTransactionFailedMessageBuilder);
        when(mockTransactionFailedMessageBuilder.withSubError(subError1)).thenReturn(mockTransactionFailedMessageBuilder);
        when(mockTransactionFailedMessageBuilder.withSubError(subError2)).thenReturn(mockTransactionFailedMessageBuilder);
        when(mockTransactionFailedMessageBuilder.withTransactionCanceledException(exception)).thenReturn(mockTransactionFailedMessageBuilder);
        when(mockTransactionFailedMessageBuilder.build()).thenReturn(errorMessage);

        assertEquals(updateBookTransactWriteRequest.createTransactionCanceledExceptionMessage(exception, BOOK), errorMessage);
    }
}
