package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.example.library.dao.ResourceRetriever;
import org.example.library.dao.dynamodb.author.converter.AuthorToItemConverter;
import org.example.library.models.Author;

import java.util.Optional;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuthorRetriever implements ResourceRetriever<Author> {

    private final Table authorsTable;

    @Inject
    public AuthorRetriever(@Named("AuthorsTable") final Table authorsTable) {
        this.authorsTable = checkNotNull(authorsTable);
    }

    @Override
    public Optional<Author> get(final UUID uuid) {
        final Item item = authorsTable.getItem(AuthorAttributes.ID.toString(), uuid.toString());
        return Optional.ofNullable(item)
                .map(AuthorToItemConverter::toAuthor);
    }
}
