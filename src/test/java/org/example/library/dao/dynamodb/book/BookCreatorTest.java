package org.example.library.dao.dynamodb.book;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.TransactWriteItemsRequest;
import com.amazonaws.services.dynamodbv2.model.TransactionCanceledException;
import org.example.library.dao.dynamodb.TransactWriteItemsRequestGenerator;
import org.example.library.models.Book;
import org.example.library.models.ImmutableBook;
import org.example.library.testutils.BookFactory;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

@Listeners(MockitoTestNGListener.class)
public class BookCreatorTest {

    private static final Book BOOK = BookFactory.randomWithDataVersionOne();

    @Mock
    private AmazonDynamoDB mockDynamoDB;
    @Mock
    private TransactWriteItemsRequestGenerator<Book> mockTransactRequestGenerator;
    @Mock
    private TransactWriteItemsRequest mockTransactWriteItemsRequest;

    private BookCreator bookCreator;

    @BeforeMethod
    public void setup() {
        when(mockTransactRequestGenerator.createRequest(BOOK)).thenReturn(mockTransactWriteItemsRequest);
        bookCreator = new BookCreator(mockDynamoDB, mockTransactRequestGenerator);
    }

    @Test
    public void GIVEN_Book_WHEN_calling_create_THEN_call_table_putItem_with_expected_arguments() {
        bookCreator.create(BOOK);
        verify(mockDynamoDB).transactWriteItems(mockTransactWriteItemsRequest);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void GIVEN_Book_with_dataVersion_not_1_WHEN_calling_create_THEN_throw_IllegalArgumentException() {
        final Book book = ImmutableBook.copyOf(BOOK)
                .withDataVersion(5);
        bookCreator.create(book);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void GIVEN_Table_throws_TransactionCanceledException_WHEN_calling_create_THEN_throw_RuntimeException() {
        doThrow(TransactionCanceledException.class).when(mockDynamoDB).transactWriteItems(mockTransactWriteItemsRequest);
        bookCreator.create(BOOK);
    }

}
