package org.example.library.models;

import org.example.library.annotations.ImmutableBuilderSettings;
import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
@ImmutableBuilderSettings
public abstract class Author implements Resource {

    @Override
    public abstract UUID getId();

    @Override
    @Value.Default
    public int getDataVersion() {
        return 1;
    }

    public abstract String getFirstName();

    public abstract String getLastName();
}
