package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.Expected;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.google.common.collect.ImmutableList;
import org.example.library.dao.dynamodb.author.AuthorAttributes;
import org.example.library.models.Author;

import java.util.function.Function;

public class AuthorToUpdateItemSpecConverter implements Function<Author, UpdateItemSpec> {

    @Override
    public UpdateItemSpec apply(final Author author) {
        final ImmutableList.Builder<AttributeUpdate> builder = new ImmutableList.Builder<AttributeUpdate>()
                .add(new AttributeUpdate(AuthorAttributes.FIRST_NAME.toString()).put(author.getFirstName()))
                .add(new AttributeUpdate(AuthorAttributes.LAST_NAME.toString()).put(author.getLastName()))
                .add(new AttributeUpdate(AuthorAttributes.DATA_VERSION.toString()).addNumeric(1));
        author.getEmail()
                .map(email -> new AttributeUpdate(AuthorAttributes.EMAIL.toString()).put(email.value()))
                .ifPresent(builder::add);
        return new UpdateItemSpec()
                .withPrimaryKey(AuthorAttributes.ID.toString(), author.getId().value())
                .withAttributeUpdate(builder.build())
                .withExpected(new Expected(AuthorAttributes.DATA_VERSION.toString()).eq(author.getDataVersion()));
    }
}
