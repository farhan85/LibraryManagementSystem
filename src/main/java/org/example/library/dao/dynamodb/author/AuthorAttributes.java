package org.example.library.dao.dynamodb.author;

public enum AuthorAttributes {

    ID("Id"),
    FIRST_NAME("FirstName"),
    LAST_NAME("LastName"),
    DATA_VERSION("DataVersion");

    private final String key;

    AuthorAttributes(final String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
