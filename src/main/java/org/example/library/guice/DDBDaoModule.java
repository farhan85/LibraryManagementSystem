package org.example.library.guice;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.base.Converter;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import org.example.library.dao.*;
import org.example.library.dao.dynamodb.TransactWriteItemBuilder;
import org.example.library.dao.dynamodb.TransactWriteItemsRequestGenerator;
import org.example.library.dao.dynamodb.TransactionFailedMessageBuilder;
import org.example.library.dao.dynamodb.author.*;
import org.example.library.dao.dynamodb.author.converter.AttributeValueMapToAuthorConverter;
import org.example.library.dao.dynamodb.author.converter.AuthorToPutItemSpecConverter;
import org.example.library.dao.dynamodb.author.converter.AuthorToUpdateItemSpecConverter;
import org.example.library.dao.dynamodb.author.converter.ItemToAuthorConverter;
import org.example.library.dao.dynamodb.book.*;
import org.example.library.dao.dynamodb.book.converter.AttributeValueMapToBookConverter;
import org.example.library.dao.dynamodb.book.converter.CreateBookTransactWriteRequest;
import org.example.library.dao.dynamodb.book.converter.ItemToBookConverter;
import org.example.library.dao.dynamodb.book.converter.UpdateBookTransactWriteRequest;
import org.example.library.models.Author;
import org.example.library.models.AuthorId;
import org.example.library.models.Book;
import org.example.library.models.BookId;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

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

    private final TypeLiteral<TransactWriteItemsRequestGenerator<Book>> CREATE_BOOK_REQUEST_GENERATOR = new TypeLiteral<>() {};
    private final TypeLiteral<TransactWriteItemsRequestGenerator<Book>> UPDATE_BOOK_REQUEST_GENERATOR = new TypeLiteral<>() {};
    private final TypeLiteral<Function<Item, Book>> DDB_ITEM_TO_BOOK_CONVERTER = new TypeLiteral<>() {};
    private final TypeLiteral<Converter<Book, Map<String, AttributeValue>>> ATTR_VALUE_MAP_TO_BOOK_CONVERTER = new TypeLiteral<>() {};
    private final TypeLiteral<ResourceCreator<Book>> BOOK_RESOURCE_CREATOR = new TypeLiteral<>() {};
    private final TypeLiteral<ResourceDeleter<BookId>> BOOK_RESOURCE_DELETER = new TypeLiteral<>() {};
    private final TypeLiteral<ResourceUpdater<Book>> BOOK_RESOURCE_UPDATER = new TypeLiteral<>() {};
    private final TypeLiteral<ResourceRetriever<BookId, Book>> BOOK_RESOURCE_RETRIEVER = new TypeLiteral<>() {};
    private final TypeLiteral<ResourceScanner<Book>> BOOK_RESOURCE_SCANNER = new TypeLiteral<>() {};
    private final TypeLiteral<ResourceDao<BookId, Book>> BOOK_RESOURCE_DAO = new TypeLiteral<>() {};
    private final TypeLiteral<ResourceDaoDelegator<BookId, Book>> BOOK_RESOURCE_DAO_DELEGATOR = new TypeLiteral<>() {};

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

        bind(CREATE_BOOK_REQUEST_GENERATOR)
                .annotatedWith(Names.named("CreateBookTransactGenerator"))
                .to(CreateBookTransactWriteRequest.class);
        bind(UPDATE_BOOK_REQUEST_GENERATOR)
                .annotatedWith(Names.named("UpdateBookTransactGenerator"))
                .to(UpdateBookTransactWriteRequest.class);
        bind(DDB_ITEM_TO_BOOK_CONVERTER).to(ItemToBookConverter.class);
        bind(ATTR_VALUE_MAP_TO_BOOK_CONVERTER).to(AttributeValueMapToBookConverter.class);
        bind(BOOK_RESOURCE_CREATOR).to(BookCreator.class);
        bind(BOOK_RESOURCE_DELETER).to(BookDeleter.class);
        bind(BOOK_RESOURCE_UPDATER).to(BookUpdater.class);
        bind(BOOK_RESOURCE_RETRIEVER).to(BookRetriever.class);
        bind(BOOK_RESOURCE_SCANNER).to(BookScanner.class);
        bind(BOOK_RESOURCE_DAO).to(BOOK_RESOURCE_DAO_DELEGATOR);
    }

    @Provides
    public Supplier<TransactWriteItemBuilder> provideTransactWriteItemBuilderSupplier() {
        return TransactWriteItemBuilder::new;
    }

    @Provides
    private Supplier<TransactionFailedMessageBuilder> provideTransactionFailedMessageBuilderSupplier() {
        return TransactionFailedMessageBuilder::new;
    }
}
