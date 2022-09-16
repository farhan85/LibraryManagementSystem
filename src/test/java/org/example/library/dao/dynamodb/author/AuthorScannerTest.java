package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import dev.failsafe.RateLimiter;
import org.example.library.models.Author;
import org.example.library.testutils.AuthorFactory;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.example.library.dao.dynamodb.author.converter.AuthorToAttributeValueMapConverter.toAttributeValueMap;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class AuthorScannerTest {

    private static final String TABLE_NAME = "TestTable";
    private static final int SCAN_LIMIT = 100;
    private static final Author AUTHOR_1 = AuthorFactory.random();
    private static final Author AUTHOR_2 = AuthorFactory.random();
    private static final Author AUTHOR_3 = AuthorFactory.random();

    @Mock
    private AmazonDynamoDB mockAmazonDynamoDB;
    @Mock
    private RateLimiter<Object> mockRateLimiter;
    @Mock
    private Map<String, AttributeValue> mockExclusiveStartKey;

    private ScanResult scanResult1;
    private ScanResult scanResult2;
    private AuthorScanner authorScanner;

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
                .withItems(List.of(toAttributeValueMap(AUTHOR_1), toAttributeValueMap(AUTHOR_2)))
                .withLastEvaluatedKey(mockExclusiveStartKey);
        scanResult2 = new ScanResult()
                .withItems(List.of(toAttributeValueMap(AUTHOR_3)));
        when(mockAmazonDynamoDB.scan(scanRequest1)).thenReturn(scanResult1);
        when(mockAmazonDynamoDB.scan(scanRequest2)).thenReturn(scanResult2);
        when(mockRateLimiter.tryAcquirePermit()).thenReturn(true);
        authorScanner = new AuthorScanner(mockAmazonDynamoDB, TABLE_NAME, mockRateLimiter);
    }

    @Test
    public void GIVEN_consumer_WHEN_calling_scan_THEN_call_consumer_with_Authors() {
        final List<Author> expected = List.of(AUTHOR_1, AUTHOR_2, AUTHOR_3);
        final List<Author> actual = new ArrayList<>();
        authorScanner.scan(actual::add);
        assertEquals(actual, expected);
    }
}
