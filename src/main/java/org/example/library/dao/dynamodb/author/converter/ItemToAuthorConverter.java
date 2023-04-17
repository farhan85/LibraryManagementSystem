package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.document.Item;
import org.example.library.dao.dynamodb.author.AuthorAttributes;
import org.example.library.models.Author;
import org.example.library.models.ImmutableAuthor;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class ItemToAuthorConverter implements Function<Item, Author> {

    @Override
    public Author apply(final Item item) {
        final ImmutableAuthor.Builder builder = ImmutableAuthor.builder()
                .withId(UUID.fromString(item.getString(AuthorAttributes.ID.toString())))
                .withFirstName(item.getString(AuthorAttributes.FIRST_NAME.toString()))
                .withLastName(item.getString(AuthorAttributes.LAST_NAME.toString()))
                .withDataVersion(item.getInt(AuthorAttributes.DATA_VERSION.toString()));
        Optional.ofNullable(item.getString(AuthorAttributes.EMAIL.toString()))
                .ifPresent(builder::withEmail);
        return builder.build();
    }
}
