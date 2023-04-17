package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.google.common.base.Converter;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.failsafe.RateLimiter;
import org.example.library.dao.ResourceScanner;
import org.example.library.models.Author;

import java.util.Map;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuthorScanner implements ResourceScanner<Author> {

    private static final int SCAN_LIMIT = 100;

    private final AmazonDynamoDB dynamoDb;
    private final String tableName;
    private final RateLimiter<Object> rateLimiter;
    private final Converter<Author, Map<String, AttributeValue>> authorConverter;

    @Inject
    public AuthorScanner(final AmazonDynamoDB dynamoDb,
                         @Named("AuthorsTableName") final String tableName,
                         @Named("AuthorsTableRateLimiter") final RateLimiter<Object> rateLimiter,
                         final Converter<Author, Map<String, AttributeValue>> authorConverter) {
        this.dynamoDb = checkNotNull(dynamoDb);
        this.tableName = checkNotNull(tableName);
        this.rateLimiter = checkNotNull(rateLimiter);
        this.authorConverter = checkNotNull(authorConverter);
    }

    @Override
    public void scan(final Consumer<Author> consumer) {
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
                        .map(authorConverter.reverse()::convert)
                        .forEach(consumer);
            }
        } while (exclusiveStartKey != null);
    }
}
