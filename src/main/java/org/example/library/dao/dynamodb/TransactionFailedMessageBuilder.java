package org.example.library.dao.dynamodb;

import com.amazonaws.services.dynamodbv2.model.CancellationReason;
import com.amazonaws.services.dynamodbv2.model.TransactionCanceledException;
import com.google.common.collect.ImmutableList;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Helper class to construct an error message when a DDB TransactWrite operation fails.
 *
 * <p>
 * The sub errors need to be added in the same order as their respective TransactWriteItems.
 * </p>
 */
public class TransactionFailedMessageBuilder {

    private String mainError;
    private final List<String> subErrors;
    private TransactionCanceledException transactionCanceledException;

    public TransactionFailedMessageBuilder() {
        this.mainError = null;
        this.subErrors = new ArrayList<>();
        this.transactionCanceledException = null;
    }

    public TransactionFailedMessageBuilder withMainError(final String error) {
        mainError = checkNotNull(error);
        return this;
    }

    public TransactionFailedMessageBuilder withSubError(final String messageFormat, final Object... args) {
        checkNotNull(messageFormat);
        checkNotNull(args);
        subErrors.add(String.format(messageFormat, args));
        return this;
    }

    public TransactionFailedMessageBuilder withTransactionCanceledException(final TransactionCanceledException e) {
        transactionCanceledException = checkNotNull(e);
        return this;
    }

    public String build() {
        checkNotNull(mainError);
        checkNotNull(transactionCanceledException);
        final ImmutableList.Builder<String> errorsBuilder = new ImmutableList.Builder<>();
        final List<CancellationReason> cancellationReasons = transactionCanceledException.getCancellationReasons();
        for (int i = 0; i < cancellationReasons.size(); i++) {
            final CancellationReason reason = cancellationReasons.get(i);
            if (!"None".equalsIgnoreCase(reason.getCode())) {
                errorsBuilder.add(getSubError(i));
            }
        }
        return String.format("%s. %s",
                mainError,
                String.join(", ", errorsBuilder.build()));
    }

    private String getSubError(final int pos) {
        try {
            return subErrors.get(pos);
        } catch (final IndexOutOfBoundsException ignored) {
            return String.format("Error with Condition #%d " +
                    "(TransactionFailedMessageBuilder did not contain detailed error message)", pos + 1);
        }
    }
}
