package org.example.library.testutils;

import org.example.library.models.Author;
import org.example.library.models.Book;
import org.example.library.models.Genre;
import org.example.library.models.ImmutableAuthorSummary;
import org.example.library.models.ImmutableBook;
import org.example.library.models.isbn.ISBNFactory;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class BookFactory {

    private static final Random RANDOM = new Random();
    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2020;
    private static final int MAX_COUNT = 20;
    private static final int MIN_DATA_VERSION = 1;
    private static final int MAX_DATA_VERSION = 1000;
    private static final Genre[] GENRES = Genre.values();
    private static final List<String> BOOK_TITLES = List.of(
            "Sailing into the Fog",
            "Ace of Hearts",
            "Mystery of the Missing Link",
            "Children of the Isle",
            "Outside the House",
            "The Lone Warrior",
            "Wolves and Blacksmiths",
            "Before the Fall",
            "Friends of the Nation",
            "The Sons of the Orb",
            "Hunting Season",
            "Fatal Tribunal",
            "The Couple in the Creek",
            "After the Final Siren",
            "Dawn of the Dark",
            "To the Moon and back",
            "The Stranger at the Door",
            "The Village Butcher",
            "Braid of Gold",
            "End of Time",
            "History of the Ancestors");

    private BookFactory() {
    }

    public static Book randomWithDataVersionOne() {
        return ImmutableBook.builder()
                .from(random())
                .withDataVersion(1)
                .build();
    }

    public static Book random() {
        return random(AuthorFactory.random());
    }

    public static Book random(final Author author) {
        return ImmutableBook.builder()
                .withId(UUID.randomUUID())
                .withIsbn(ISBNFactory.randomISBN10())
                .withTitle(BOOK_TITLES.get(RANDOM.nextInt(BOOK_TITLES.size())))
                .withAuthor(ImmutableAuthorSummary.builder()
                        .withId(author.getId())
                        .withFirstName(author.getFirstName())
                        .withLastName(author.getLastName())
                        .build())
                .withYear(RANDOM.nextInt(MIN_YEAR, MAX_YEAR))
                .withGenre(GENRES[RANDOM.nextInt(GENRES.length)])
                .withCount(RANDOM.nextInt(MAX_COUNT))
                .withDataVersion(RANDOM.nextInt(MIN_DATA_VERSION, MAX_DATA_VERSION))
                .build();
    }
}
