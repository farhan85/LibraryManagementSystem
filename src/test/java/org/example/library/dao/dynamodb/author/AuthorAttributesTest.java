package org.example.library.dao.dynamodb.author;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class AuthorAttributesTest {

    @Test
    public void GIVEN_AuthorAttributes_enum_WHEN_calling_toString_THEN_return_table_attribute_name() {
        assertEquals(AuthorAttributes.ID.toString(), "Id");
        assertEquals(AuthorAttributes.FIRST_NAME.toString(), "FirstName");
        assertEquals(AuthorAttributes.LAST_NAME.toString(), "LastName");
        assertEquals(AuthorAttributes.DATA_VERSION.toString(), "DataVersion");
    }
}
