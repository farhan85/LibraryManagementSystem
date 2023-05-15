package org.example.library.dao.dynamodb.book;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.google.common.base.Converter;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.failsafe.RateLimiter;
import org.example.library.dao.ResourceScanner;
import org.example.library.models.Book;

import java.util.Map;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

public class BookScanner implements ResourceScanner<Book> {

    private static final int SCAN_LIMIT = 100;

    private final AmazonDynamoDB dynamoDb;
    private final String tableName;
    private final RateLimiter<Object> rateLimiter;
    private final Converter<Book, Map<String, AttributeValue>> attributeValueMapConverter;

    @Inject
    public BookScanner(final AmazonDynamoDB dynamoDb,
                       @Named("BooksTableName") final String tableName,
                       final RateLimiter<Object> rateLimiter,
                       final Converter<Book, Map<String, AttributeValue>> attributeValueMapConverter) {
        this.dynamoDb = checkNotNull(dynamoDb);
        this.tableName = checkNotNull(tableName);
        this.rateLimiter = checkNotNull(rateLimiter);
        this.attributeValueMapConverter = checkNotNull(attributeValueMapConverter);
    }

    @Override
    public void scan(final Consumer<Book> consumer) {
        Map<String, AttributeValue> exclusiveStartKey = null;
        do {
            if (rateLimiter.tryAcquirePermit()) {
                final ScanRequest scanRequest = new ScanRequest()
                        .withTableName(tableName)
                        .withLimit(SCAN_LIMIT)
                        .withExclusiveStartKey(exclusiveStartKey);
                final ScanResult result = dynamoDb.scan(scanRequest);
                exclusiveStartKey = result.getLastEvaluatedKey();

                result.getItems()
                        .stream()
                        .map(attributeValueMapConverter.reverse())
                        .forEach(consumer);
            }
        } while (exclusiveStartKey != null);
    }
}
