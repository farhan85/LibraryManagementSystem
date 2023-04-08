package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.example.library.dao.ResourceCreator;
import org.example.library.models.Author;

import java.util.ConcurrentModificationException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.example.library.dao.dynamodb.author.converter.AuthorToItemConverter.toItem;

public class AuthorCreator implements ResourceCreator<Author> {

    private final Table authorsTable;

    @Inject
    public AuthorCreator(@Named("AuthorsTable") final Table authorsTable) {
        this.authorsTable = checkNotNull(authorsTable);
    }

    @Override
    public void create(final Author author) {
        checkArgument(author.getDataVersion() == 1);
        try {
            authorsTable.putItem(new PutItemSpec()
                    .withItem(toItem(author))
                    .withConditionExpression("attribute_not_exists(#id)")
                    .withNameMap(new NameMap().with("#id", AuthorAttributes.ID.toString()))
            );
        } catch (final ConditionalCheckFailedException e) {
            throw new ConcurrentModificationException(
                    String.format("Author already exists. AuthorId=%s", author.getId().value()),
                    e);
        }
    }
}
