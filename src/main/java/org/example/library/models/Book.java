package org.example.library.models;

import org.example.library.annotations.ImmutableBuilderSettings;
import org.example.library.models.isbn.ISBN;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableBuilderSettings
public abstract class Book implements Resource<BookId> {

    @Override
    public abstract BookId getId();

    @Override
    @Value.Default
    public int getDataVersion() {
        return 1;
    }

    public abstract ISBN getIsbn();

    public abstract String getTitle();

    public abstract AuthorSummary getAuthor();

    public abstract Genre getGenre();

    public abstract int getYear();

    public abstract int getCount();
}
