package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.collect.ImmutableMap;
import org.example.library.models.Author;
import org.example.library.models.ImmutableAuthor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.example.library.dao.dynamodb.author.AuthorAttributes.DATA_VERSION;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.EMAIL;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.FIRST_NAME;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.ID;
import static org.example.library.dao.dynamodb.author.AuthorAttributes.LAST_NAME;

public class AuthorToAttributeValueMapConverter {

    public static Author toAuthor(final Map<String, AttributeValue> item) {
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

    public static Map<String, AttributeValue> toAttributeValueMap(final Author author) {
        final ImmutableMap.Builder<String, AttributeValue> builder = new ImmutableMap.Builder<String, AttributeValue>()
                .put(ID.toString(), new AttributeValue().withS(author.getId().value()))
                .put(FIRST_NAME.toString(), new AttributeValue().withS(author.getFirstName()))
                .put(LAST_NAME.toString(), new AttributeValue().withS(author.getLastName()))
                .put(DATA_VERSION.toString(), new AttributeValue().withN(Integer.toString(author.getDataVersion())));
        author.getEmail().ifPresent(email -> builder.put(EMAIL.toString(), new AttributeValue().withS(email.value())));
        return builder.build();
    }
}
