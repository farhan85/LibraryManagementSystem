package org.example.library.dao.dynamodb.book;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.example.library.models.Book;
import org.example.library.models.BookId;
import org.example.library.models.ImmutableBookId;
import org.example.library.testutils.BookFactory;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class BookRetrieverTest {

    private static final Book BOOK = BookFactory.random();
    private static final BookId INVALID_ID = ImmutableBookId.of(UUID.randomUUID());

    @Mock
    private Table mockBooksTable;
    @Mock
    private Function<Item, Book> mockItemToBookConverter;
    @Mock
    private Item mockBookItem;
    private BookRetriever bookRetriever;

    @BeforeMethod
    public void setup() {
        bookRetriever = new BookRetriever(mockBooksTable, mockItemToBookConverter);
    }

    @Test
    public void GIVEN_uuid_WHEN_calling_get_THEN_return_Book() {
        when(mockBooksTable.getItem(BookAttributes.ID.toString(), BOOK.getId().value()))
                .thenReturn(mockBookItem);
        when(mockItemToBookConverter.apply(mockBookItem)).thenReturn(BOOK);
        assertEquals(bookRetriever.get(BOOK.getId()), Optional.of(BOOK));
    }

    @Test
    public void GIVEN_uuid_and_item_does_not_exist_WHEN_calling_get_THEN_return_empty_optional() {
        when(mockBooksTable.getItem(BookAttributes.ID.toString(), INVALID_ID.value()))
                .thenReturn(null);
        assertEquals(bookRetriever.get(INVALID_ID), Optional.empty());
    }
}
