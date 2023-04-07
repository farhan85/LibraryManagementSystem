package org.example.library.dao.dynamodb.author;

import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.example.library.dao.ResourceUpdater;
import org.example.library.models.Author;

import java.util.ConcurrentModificationException;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuthorUpdater implements ResourceUpdater<Author> {

    private final Table authorsTable;

    @Inject
    public AuthorUpdater(@Named("AuthorsTable") final Table authorsTable) {
        this.authorsTable = checkNotNull(authorsTable);
    }

    @Override
    public void update(final Author author) {
        try {
            updateDDBItem(author);
        } catch (final ConditionalCheckFailedException e) {
            final String message = String.format("Author item modified by another process. AuthorId=%s dataVersion=%d",
                    author.getId(),
                    author.getDataVersion());
            throw new ConcurrentModificationException(message, e);
        }
    }

    public void updateDDBItem(final Author author) {
        final String updateExpression = "set " + String.join(",",
                "#first_name = :first_name",
                "#last_name = :last_name",
                "#data_version = #data_version + :increment");
        final String conditionExpression = "#data_version = :data_version";
        final NameMap nameMap = new NameMap()
                .with("#first_name", AuthorAttributes.FIRST_NAME.toString())
                .with("#last_name", AuthorAttributes.LAST_NAME.toString())
                .with("#data_version", AuthorAttributes.DATA_VERSION.toString());
        final ValueMap valueMap = new ValueMap()
                .withString(":first_name", author.getFirstName())
                .withString(":last_name", author.getLastName())
                .withNumber(":data_version", author.getDataVersion())
                .withNumber(":increment", 1);

        authorsTable.updateItem(new UpdateItemSpec()
                .withPrimaryKey(AuthorAttributes.ID.toString(), author.getId().toString())
                .withUpdateExpression(updateExpression)
                .withConditionExpression(conditionExpression)
                .withNameMap(nameMap)
                .withValueMap(valueMap));
    }
}
