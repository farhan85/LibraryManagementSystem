package org.example.library.guice;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import dev.failsafe.RateLimiter;

import java.time.Duration;
import java.util.Objects;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

public class AwsClientModule extends AbstractModule {
    private final Regions region;

    public AwsClientModule(final Regions region) {
        this.region = checkNotNull(region);
    }

    public AwsClientModule() {
        this.region = Stream.of(System.getProperty("region"), System.getenv("AWS_DEFAULT_REGION"))
                .filter(Objects::nonNull)
                .findFirst()
                .map(Regions::fromName)
                .orElseThrow(() -> new RuntimeException("No AWS region specified"));
    }

    @Override
    protected void configure() {
        bindConstant().annotatedWith(Names.named("AuthorsTableName")).to("Authors");
    }

    @Provides
    @Singleton
    public AWSCredentialsProvider getCredentials() {
        return new ProfileCredentialsProvider("iam_user_admin");
    }

    @Provides
    public AmazonDynamoDB AmazonDynamoDB(final AWSCredentialsProvider credentials) {
        return AmazonDynamoDBClient.builder()
                .withCredentials(credentials)
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
    @Named("AuthorsTableRateLimiter")
    public RateLimiter<Object> provideAuthorsTableRateLimiter() {
        return RateLimiter.smoothBuilder(5, Duration.ofSeconds(1))
                .withMaxWaitTime(Duration.ofSeconds(10))
                .build();
    }
}
