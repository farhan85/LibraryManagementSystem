package org.example.library.guice;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
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

public class AwsClientModule extends AbstractModule {

    @Override
    protected void configure() {
        bindConstant().annotatedWith(Names.named("AuthorsTableName")).to("Authors");
    }

    @Provides
    public AmazonDynamoDB AmazonDynamoDB() {
        return AmazonDynamoDBClient.builder()
                .withCredentials(new ProfileCredentialsProvider("iam_user_admin"))
                .withRegion("us-west-2")
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
    @Named("AuthorsTableRateLimiter")
    public RateLimiter<Object> provideAuthorsTableRateLimiter() {
        return RateLimiter.smoothBuilder(5, Duration.ofSeconds(1))
                .withMaxWaitTime(Duration.ofSeconds(10))
                .build();
    }
}
