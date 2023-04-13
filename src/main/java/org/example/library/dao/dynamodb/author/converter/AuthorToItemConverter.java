package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.Expected;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import org.example.library.dao.dynamodb.author.AuthorAttributes;
import org.example.library.models.Author;
import org.example.library.models.ImmutableAuthor;

import java.util.UUID;

public class AuthorToItemConverter {

    public static Author toAuthor(final Item item) {
        return ImmutableAuthor.builder()
                .withId(UUID.fromString(item.getString(AuthorAttributes.ID.toString())))
                .withFirstName(item.getString(AuthorAttributes.FIRST_NAME.toString()))
                .withLastName(item.getString(AuthorAttributes.LAST_NAME.toString()))
                .withDataVersion(item.getInt(AuthorAttributes.DATA_VERSION.toString()))
                .build();
    }

    public static PutItemSpec toPutItemSpec(final Author author) {
        return new PutItemSpec()
                .withItem(new Item()
                        .withPrimaryKey(AuthorAttributes.ID.toString(), author.getId().value())
                        .withString(AuthorAttributes.FIRST_NAME.toString(), author.getFirstName())
                        .withString(AuthorAttributes.LAST_NAME.toString(), author.getLastName())
                        .withNumber(AuthorAttributes.DATA_VERSION.toString(), author.getDataVersion()))
                .withExpected(
                        new Expected(AuthorAttributes.ID.toString()).notExist());
    }

    public static UpdateItemSpec toUpdateItemSpec(final Author author) {
        return new UpdateItemSpec()
                .withPrimaryKey(AuthorAttributes.ID.toString(), author.getId().value())
                .withAttributeUpdate(
                        new AttributeUpdate(AuthorAttributes.FIRST_NAME.toString()).put(author.getFirstName()),
                        new AttributeUpdate(AuthorAttributes.LAST_NAME.toString()).put(author.getLastName()),
                        new AttributeUpdate(AuthorAttributes.DATA_VERSION.toString()).addNumeric(1))
                .withExpected(
                        new Expected(AuthorAttributes.DATA_VERSION.toString()).eq(author.getDataVersion()));
    }
}
