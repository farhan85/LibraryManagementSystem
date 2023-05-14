package org.example.library.dao.dynamodb.book;

public enum BookAttributes {

    ID("Id"),
    ISBN("Isbn"),
    TITLE("Title"),
    AUTHOR_ID("AuthorId"),
    AUTHOR_FIRST_NAME("AuthorFirstName"),
    AUTHOR_LAST_NAME("AuthorLastName"),
    GENRE("Genre"),
    YEAR("Year"),
    COUNT("Count"),
    DATA_VERSION("DataVersion");

    private final String key;

    BookAttributes(final String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
