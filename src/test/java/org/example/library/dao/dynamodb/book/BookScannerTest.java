package org.example.library.dao.dynamodb.book;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.google.common.base.Converter;
import dev.failsafe.RateLimiter;
import org.example.library.models.Book;
import org.example.library.testutils.BookFactory;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class BookScannerTest {

    private static final String TABLE_NAME = "TestTable";
    private static final int SCAN_LIMIT = 100;
    private static final Book BOOK_1 = BookFactory.random();
    private static final Book BOOK_2 = BookFactory.random();
    private static final Book BOOK_3 = BookFactory.random();

    @Mock
    private AmazonDynamoDB mockAmazonDynamoDB;
    @Mock
    private RateLimiter<Object> mockRateLimiter;
    @Mock
    private Map<String, AttributeValue> mockExclusiveStartKey;
    @Mock
    private Converter<Book, Map<String, AttributeValue>> mockBookConverter;
    @Mock
    private Converter<Map<String, AttributeValue>, Book> mockAttributeValueMapConverter;
    @Mock
    private Map<String, AttributeValue> mockBook1Item;
    @Mock
    private Map<String, AttributeValue> mockBook2Item;
    @Mock
    private Map<String, AttributeValue> mockBook3Item;

    private ScanResult scanResult1;
    private ScanResult scanResult2;
    private BookScanner bookScanner;

    @BeforeMethod
    public void setup() {
        final ScanRequest scanRequest1 = new ScanRequest()
                .withTableName(TABLE_NAME)
                .withLimit(SCAN_LIMIT);
        final ScanRequest scanRequest2 = new ScanRequest()
                .withTableName(TABLE_NAME)
                .withLimit(SCAN_LIMIT)
                .withExclusiveStartKey(mockExclusiveStartKey);
        scanResult1 = new ScanResult()
                .withItems(List.of(mockBook1Item, mockBook2Item))
                .withLastEvaluatedKey(mockExclusiveStartKey);
        scanResult2 = new ScanResult()
                .withItems(List.of(mockBook3Item));
        when(mockAmazonDynamoDB.scan(scanRequest1)).thenReturn(scanResult1);
        when(mockAmazonDynamoDB.scan(scanRequest2)).thenReturn(scanResult2);
        when(mockRateLimiter.tryAcquirePermit()).thenReturn(true);
        when(mockBookConverter.reverse()).thenReturn(mockAttributeValueMapConverter);
        when(mockAttributeValueMapConverter.convert(mockBook1Item)).thenReturn(BOOK_1);
        when(mockAttributeValueMapConverter.convert(mockBook2Item)).thenReturn(BOOK_2);
        when(mockAttributeValueMapConverter.convert(mockBook3Item)).thenReturn(BOOK_3);
        bookScanner = new BookScanner(mockAmazonDynamoDB, TABLE_NAME, mockRateLimiter, mockBookConverter);
    }

    @Test
    public void GIVEN_consumer_WHEN_calling_scan_THEN_call_consumer_with_Books() {
        final List<Book> expected = List.of(BOOK_1, BOOK_2, BOOK_3);
        final List<Book> actual = new ArrayList<>();
        bookScanner.scan(actual::add);
        assertEquals(actual, expected);
    }
}
