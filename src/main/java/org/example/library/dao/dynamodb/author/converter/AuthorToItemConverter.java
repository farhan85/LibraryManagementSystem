package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.document.Item;
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

    public static Item toItem(final Author author) {
        return new Item()
                .withPrimaryKey(AuthorAttributes.ID.toString(), author.getId().value())
                .withString(AuthorAttributes.FIRST_NAME.toString(), author.getFirstName())
                .withString(AuthorAttributes.LAST_NAME.toString(), author.getLastName())
                .withNumber(AuthorAttributes.DATA_VERSION.toString(), author.getDataVersion());
    }
}
