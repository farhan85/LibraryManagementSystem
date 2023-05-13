package org.example.library.models;

public enum Genre {

    CRIME("Crime"),
    FANTASY("Fantasy"),
    MYSTERY("Mystery"),
    ROMANCE("Romance"),
    SCI_FI("Sci-Fi");

    private final String displayName;

    Genre(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Genre fromString(final String value) {
        return Genre.valueOf(value.toUpperCase());
    }
}
