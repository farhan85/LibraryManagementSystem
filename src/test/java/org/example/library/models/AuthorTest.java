package org.example.library.models;

import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.AssertJUnit.assertEquals;

public class AuthorTest {

    @Test
    public void GIVEN_no_dataVersion_given_WHEN_creating_Author_THEN_return_Author_with_dataVersion_one() {
        final Author author = ImmutableAuthor.builder()
                .withId(UUID.randomUUID())
                .withFirstName("first-name")
                .withLastName("last-name")
                .build();
        assertEquals(author.getDataVersion(), 1);
    }
}
