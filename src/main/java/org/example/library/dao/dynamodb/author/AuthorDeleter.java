package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.document.Table;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.example.library.dao.ResourceDeleter;
import org.example.library.models.Author;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuthorDeleter implements ResourceDeleter<Author> {

    private final Table authorsTable;

    @Inject
    public AuthorDeleter(@Named("AuthorsTable") final Table authorsTable) {
        this.authorsTable = checkNotNull(authorsTable);
    }

    @Override
    public void delete(final UUID uuid) {
        authorsTable.deleteItem(AuthorAttributes.ID.toString(), uuid.toString());
    }
}
