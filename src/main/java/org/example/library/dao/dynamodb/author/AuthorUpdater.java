package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.example.library.dao.ResourceUpdater;
import org.example.library.models.Author;

import java.util.ConcurrentModificationException;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.example.library.dao.dynamodb.author.converter.AuthorToItemSpecConverter.toUpdateItemSpec;

public class AuthorUpdater implements ResourceUpdater<Author> {

    private final Table authorsTable;

    @Inject
    public AuthorUpdater(@Named("AuthorsTable") final Table authorsTable) {
        this.authorsTable = checkNotNull(authorsTable);
    }

    @Override
    public void update(final Author author) {
        try {
            final UpdateItemSpec updateItemSpec = toUpdateItemSpec(author)
                    .withReturnValues(ReturnValue.NONE);
            authorsTable.updateItem(updateItemSpec);
        } catch (final ConditionalCheckFailedException e) {
            final String message = String.format("Author item modified by another process. AuthorId=%s dataVersion=%d",
                    author.getId(),
                    author.getDataVersion());
            throw new ConcurrentModificationException(message, e);
        }
    }
}
