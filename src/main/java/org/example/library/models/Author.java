package org.example.library.models;

import org.example.library.annotations.ImmutableBuilderSettings;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
@ImmutableBuilderSettings
public abstract class Author implements Resource<AuthorId> {

    @Override
    public abstract AuthorId getId();

    @Override
    @Value.Default
    public int getDataVersion() {
        return 1;
    }

    public abstract String getFirstName();

    public abstract String getLastName();

    public abstract Optional<Email> getEmail();
}
