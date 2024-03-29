package org.example.library.testutils;

import org.example.library.models.Author;
import org.example.library.models.ImmutableAuthor;
import org.example.library.models.ImmutableAuthorId;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class AuthorFactory {

    private static final Random RANDOM = new Random();
    private static final int MIN_DATA_VERSION = 1;
    private static final int MAX_DATA_VERSION = 1000;

    private static final List<String> FIRST_NAMES = List.of(
            "Aaron", "Alice", "Anthony", "Ben", "Daniel", "Dylan", "Elizabeth", "Emily", "Emma", "Hannah",
            "Henry", "Jack", "John", "Lucy", "Luke", "Madeline", "Michael", "Olivia", "Sarah", "Sophie");

    private static final List<String> LAST_NAMES = List.of(
            "Andersen", "Brown", "Collins", "Dickson", "Farley", "Fitzpatrick", "Glover", "Hawkins", "Holland", "Holloway",
            "Jarvis", "Lara", "Martinez", "Mckee", "Montes", "Nixon", "Peterson", "Richardson", "Romero", "Warner");

    private AuthorFactory() {
    }

    public static Author randomWithDataVersionOne() {
        return ImmutableAuthor.builder()
                .from(random())
                .withDataVersion(1)
                .build();
    }

    public static Author random() {
        return ImmutableAuthor.builder()
                .from(randomNonOptionals())
                .withEmail(EmailFactory.random())
                .build();
    }

    public static Author randomNonOptionals() {
        return ImmutableAuthor.builder()
                .withId(ImmutableAuthorId.of(UUID.randomUUID()))
                .withFirstName(FIRST_NAMES.get(RANDOM.nextInt(FIRST_NAMES.size())))
                .withLastName(LAST_NAMES.get(RANDOM.nextInt(LAST_NAMES.size())))
                .withDataVersion(RANDOM.nextInt(MIN_DATA_VERSION, MAX_DATA_VERSION))
                .build();
    }
}
