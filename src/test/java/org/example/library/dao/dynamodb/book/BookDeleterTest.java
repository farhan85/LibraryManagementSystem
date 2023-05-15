package org.example.library.dao.dynamodb.book;

import com.amazonaws.services.dynamodbv2.document.Table;
import org.example.library.models.Book;
import org.example.library.testutils.BookFactory;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verify;

@Listeners(MockitoTestNGListener.class)
public class BookDeleterTest {

    @Mock
    private Table mockBooksTable;

    private BookDeleter bookDeleter;

    @BeforeMethod
    public void setup() {
        bookDeleter = new BookDeleter(mockBooksTable);
    }

    @Test
    public void GIVEN_bookId_WHEN_calling_delete_THEN_call_table_deleteItem_with_expected_arguments() {
        final Book book = BookFactory.random();
        bookDeleter.delete(book.getId());
        verify(mockBooksTable).deleteItem(BookAttributes.ID.toString(), book.getId().value());
    }
}
