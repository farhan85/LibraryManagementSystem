package org.example.library.models;

import org.example.library.annotations.ImmutableBuilderSettings;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableBuilderSettings
public abstract class AuthorSummary {

    public abstract AuthorId getId();

    public abstract String getFirstName();

    public abstract String getLastName();
}
