package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.collect.ImmutableMap;
import org.example.library.dao.dynamodb.author.AuthorAttributes;
import org.example.library.models.Author;
import org.example.library.models.ImmutableAuthor;

import java.util.Map;
import java.util.UUID;

public class AuthorToAttributeValueMapConverter {

    public static Author toAuthor(final Map<String, AttributeValue> item) {
        return ImmutableAuthor.builder()
                .withId(UUID.fromString(item.get(AuthorAttributes.ID.toString()).getS()))
                .withFirstName(item.get(AuthorAttributes.FIRST_NAME.toString()).getS())
                .withLastName(item.get(AuthorAttributes.LAST_NAME.toString()).getS())
                .withDataVersion(Integer.parseInt(item.get(AuthorAttributes.DATA_VERSION.toString()).getN()))
                .build();
    }

    public static Map<String, AttributeValue> toAttributeValueMap(final Author author) {
        return ImmutableMap.of(
                AuthorAttributes.ID.toString(), new AttributeValue().withS(author.getId().value()),
                AuthorAttributes.FIRST_NAME.toString(), new AttributeValue().withS(author.getFirstName()),
                AuthorAttributes.LAST_NAME.toString(), new AttributeValue().withS(author.getLastName()),
                AuthorAttributes.DATA_VERSION.toString(), new AttributeValue().withN(Integer.toString(author.getDataVersion())));
    }
}
