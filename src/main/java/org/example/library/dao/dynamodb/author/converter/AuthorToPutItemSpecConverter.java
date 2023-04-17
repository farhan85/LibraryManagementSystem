package org.example.library.dao.dynamodb.author.converter;

import com.amazonaws.services.dynamodbv2.document.Expected;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import org.example.library.dao.dynamodb.author.AuthorAttributes;
import org.example.library.models.Author;

import java.util.function.Function;

public class AuthorToPutItemSpecConverter implements Function<Author, PutItemSpec> {

    @Override
    public PutItemSpec apply(final Author author) {
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
}
