package org.example.library.models;

import org.hazlewood.connor.bottema.emailaddress.EmailAddressValidator;
import org.immutables.value.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value.Immutable(builder = false)
public abstract class Email {

    @Value.Parameter
    public abstract String value();

    @Value.Check
    protected void validate() {
        final String email = value();
        checkArgument(EmailAddressValidator.isValid(email), "Invalid email. Value=" + email);
    }
}
