package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.example.library.models.Author;
import org.example.library.models.ImmutableAuthor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.example.library.dao.dynamodb.author.AuthorAttributes.DATA_VERSION;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.EMAIL;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.FIRST_NAME;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.ID;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.LAST_NAME;

public class AttributeValueMapToAuthorConverter implements Function<Map<String, AttributeValue>, Author> {

    @Override
    public Author apply(final Map<String, AttributeValue> item) {
        final ImmutableAuthor.Builder builder = ImmutableAuthor.builder()
                .withId(UUID.fromString(item.get(ID.toString()).getS()))
                .withFirstName(item.get(FIRST_NAME.toString()).getS())
                .withLastName(item.get(LAST_NAME.toString()).getS())
                .withDataVersion(Integer.parseInt(item.get(DATA_VERSION.toString()).getN()));
        Optional.ofNullable(item.get(EMAIL.toString()))
                .map(AttributeValue::getS)
                .ifPresent(builder::withEmail);
        return builder.build();
    }
}
