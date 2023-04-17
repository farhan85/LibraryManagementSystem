package org.example.library.guice;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.example.library.dao.ResourceCreator;
import org.example.library.dao.ResourceDao;
import org.example.library.dao.ResourceDaoDelegator;
import org.example.library.dao.ResourceDeleter;
import org.example.library.dao.ResourceRetriever;
import org.example.library.dao.ResourceScanner;
import org.example.library.dao.ResourceUpdater;
import org.example.library.dao.dynamodb.author.AuthorCreator;
import org.example.library.dao.dynamodb.author.AuthorDeleter;
import org.example.library.dao.dynamodb.author.AuthorRetriever;
import org.example.library.dao.dynamodb.author.AuthorScanner;
import org.example.library.dao.dynamodb.author.AuthorUpdater;
import org.example.library.dao.dynamodb.author.converter.AttributeValueMapToAuthorConverter;
import org.example.library.dao.dynamodb.author.converter.AuthorToPutItemSpecConverter;
import org.example.library.dao.dynamodb.author.converter.AuthorToUpdateItemSpecConverter;
import org.example.library.dao.dynamodb.author.converter.ItemToAuthorConverter;
import org.example.library.models.Author;
import org.example.library.models.AuthorId;

import java.util.Map;
import java.util.function.Function;

public class DDBDaoModule extends AbstractModule {

    private final TypeLiteral<Function<Author, PutItemSpec>> AUTHOR_TO_PUT_ITEM_SPEC_CONVERTER = new TypeLiteral<>() {};
    private final TypeLiteral<Function<Author, UpdateItemSpec>> AUTHOR_TO_UPDATE_ITEM_SPEC_CONVERTER = new TypeLiteral<>() {};
    private final TypeLiteral<Function<Item, Author>> DDB_ITEM_TO_AUTHOR_CONVERTER = new TypeLiteral<>() {};
    private final TypeLiteral<Function<Map<String, AttributeValue>, Author>> ATTR_VALUE_MAP_TO_AUTHOR_CONVERTER = new TypeLiteral<>() {};
    private final TypeLiteral<ResourceCreator<Author>> AUTHOR_RESOURCE_CREATOR = new TypeLiteral<>() {};
    private final TypeLiteral<ResourceDeleter<AuthorId>> AUTHOR_RESOURCE_DELETER = new TypeLiteral<>() {};
    private final TypeLiteral<ResourceUpdater<Author>> AUTHOR_RESOURCE_UPDATER = new TypeLiteral<>() {};
    private final TypeLiteral<ResourceRetriever<AuthorId, Author>> AUTHOR_RESOURCE_RETRIEVER = new TypeLiteral<>() {};
    private final TypeLiteral<ResourceScanner<Author>> AUTHOR_RESOURCE_SCANNER = new TypeLiteral<>() {};
    private final TypeLiteral<ResourceDao<AuthorId, Author>> AUTHOR_RESOURCE_DAO = new TypeLiteral<>() {};
    private final TypeLiteral<ResourceDaoDelegator<AuthorId, Author>> AUTHOR_RESOURCE_DAO_DELEGATOR = new TypeLiteral<>() {};

    @Override
    protected void configure() {
        bind(AUTHOR_TO_PUT_ITEM_SPEC_CONVERTER).to(AuthorToPutItemSpecConverter.class);
        bind(AUTHOR_TO_UPDATE_ITEM_SPEC_CONVERTER).to(AuthorToUpdateItemSpecConverter.class);
        bind(DDB_ITEM_TO_AUTHOR_CONVERTER).to(ItemToAuthorConverter.class);
        bind(ATTR_VALUE_MAP_TO_AUTHOR_CONVERTER).to(AttributeValueMapToAuthorConverter.class);
        bind(AUTHOR_RESOURCE_CREATOR).to(AuthorCreator.class);
        bind(AUTHOR_RESOURCE_DELETER).to(AuthorDeleter.class);
        bind(AUTHOR_RESOURCE_UPDATER).to(AuthorUpdater.class);
        bind(AUTHOR_RESOURCE_RETRIEVER).to(AuthorRetriever.class);
        bind(AUTHOR_RESOURCE_SCANNER).to(AuthorScanner.class);
        bind(AUTHOR_RESOURCE_DAO).to(AUTHOR_RESOURCE_DAO_DELEGATOR);
    }
}
