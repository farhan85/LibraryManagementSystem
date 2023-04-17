package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.example.library.dao.ResourceRetriever;
import org.example.library.models.Author;
import org.example.library.models.AuthorId;

import java.util.Optional;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuthorRetriever implements ResourceRetriever<AuthorId, Author> {

    private final Table authorsTable;
    private final Function<Item, Author> itemToAuthorConverter;

    @Inject
    public AuthorRetriever(@Named("AuthorsTable") final Table authorsTable,
                           final Function<Item, Author> itemToAuthorConverter) {
        this.authorsTable = checkNotNull(authorsTable);
        this.itemToAuthorConverter = checkNotNull(itemToAuthorConverter);
    }

    @Override
    public Optional<Author> get(final AuthorId authorId) {
        final Item item = authorsTable.getItem(AuthorAttributes.ID.toString(), authorId.value());
        return Optional.ofNullable(item)
                .map(itemToAuthorConverter);
    }
}
