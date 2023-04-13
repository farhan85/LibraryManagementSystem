package org.example.library.testutils;

import org.example.library.models.Email;
import org.example.library.models.ImmutableEmail;

import java.util.List;
import java.util.Random;

public final class EmailFactory {

    private static final Random RANDOM = new Random();

    private static final List<String> WORDS = List.of(
            "account", "ants", "appliance", "blade", "committee", "ducks", "fairies", "flower", "jellyfish", "kettle",
            "ladybug", "leaf", "letter", "oranges", "passenger", "person", "powder", "scissors", "store", "zoo");

    private EmailFactory() {
    }

    public static Email random() {
        final String email = String.format("%s%d@test.com",
                WORDS.get(RANDOM.nextInt(WORDS.size())),
                RANDOM.nextInt(100));
        return ImmutableEmail.of(email);
    }
}
