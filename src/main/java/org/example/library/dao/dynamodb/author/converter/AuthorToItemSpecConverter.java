package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.Expected;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.google.common.collect.ImmutableList;
import org.example.library.dao.dynamodb.author.AuthorAttributes;
import org.example.library.models.Author;

public class AuthorToItemSpecConverter {

    public static PutItemSpec toPutItemSpec(final Author author) {
        final Item item = new Item()
                .withPrimaryKey(AuthorAttributes.ID.toString(), author.getId().value())
                .withString(AuthorAttributes.FIRST_NAME.toString(), author.getFirstName())
                .withString(AuthorAttributes.LAST_NAME.toString(), author.getLastName())
                .withNumber(AuthorAttributes.DATA_VERSION.toString(), author.getDataVersion());
        author.getEmail().ifPresent(email -> item.withString(AuthorAttributes.EMAIL.toString(), email.value()));
        return new PutItemSpec()
                .withItem(item)
                .withExpected(new Expected(AuthorAttributes.ID.toString()).notExist());
    }

    public static UpdateItemSpec toUpdateItemSpec(final Author author) {
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
