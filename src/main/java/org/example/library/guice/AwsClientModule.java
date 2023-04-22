package org.example.library.guice;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import dev.failsafe.RateLimiter;

import java.time.Duration;

import static com.google.common.base.Preconditions.checkNotNull;

public class AwsClientModule extends AbstractModule {

    private final Regions region;
    private final AWSCredentialsProvider credentialsProvider;

    public AwsClientModule(final Regions region, final AWSCredentialsProvider credentialsProvider) {
        this.region = checkNotNull(region);
        this.credentialsProvider = checkNotNull(credentialsProvider);
    }

    @Override
    protected void configure() {
        bindConstant().annotatedWith(Names.named("AuthorsTableName")).to("Authors");
    }

    @Provides
    public AmazonDynamoDB provideAmazonDynamoDB() {
        return AmazonDynamoDBClient.builder()
                .withCredentials(credentialsProvider)
                .withRegion(region)
                .build();
    }

    @Provides
    public DynamoDB provideDynamoDB(final AmazonDynamoDB client) {
        return new DynamoDB(client);
    }

    @Provides
    @Named("AuthorsTable")
    public Table provideAuthorsTable(final DynamoDB dynamoDb, @Named("AuthorsTableName") final String tableName) {
        return dynamoDb.getTable(tableName);
    }

    @Provides
    public RateLimiter<Object> provideAuthorsTableRateLimiter() {
        return RateLimiter.smoothBuilder(5, Duration.ofSeconds(1))
                .withMaxWaitTime(Duration.ofSeconds(10))
                .build();
    }
}
