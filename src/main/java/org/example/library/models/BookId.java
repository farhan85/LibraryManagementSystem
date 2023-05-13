package org.example.library.models;

import org.example.library.annotations.ImmutableBuilderSettings;
import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
@ImmutableBuilderSettings
public abstract class BookId implements ResourceId {

    @Override
    @Value.Parameter
    public abstract UUID uuid();

}
