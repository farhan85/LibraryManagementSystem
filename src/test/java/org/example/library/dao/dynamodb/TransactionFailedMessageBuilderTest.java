package org.example.library.dao.dynamodb;

import com.amazonaws.services.dynamodbv2.model.CancellationReason;
import com.amazonaws.services.dynamodbv2.model.TransactionCanceledException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TransactionFailedMessageBuilderTest {

    private static final String MAIN_ERROR = "main-error";
    private static final String SUB_ERROR_1 = "sub-error-1";
    private static final String SUB_ERROR_2 = "sub-error-2";

    private TransactionFailedMessageBuilder messageBuilder;

    @BeforeMethod
    public void setup() {
        messageBuilder = new TransactionFailedMessageBuilder()
                .withMainError(MAIN_ERROR)
                .withSubError(SUB_ERROR_1)
                .withSubError(SUB_ERROR_2);
    }

    @Test
    public void GIVEN_mainError_and_subErrors_and_firstConditionFailed_WHEN_calling_build_THEN_return_expected_string() {
        final TransactionCanceledException exception = new TransactionCanceledException("test-message")
                .withCancellationReasons(
                        new CancellationReason().withCode("failed"),
                        new CancellationReason().withCode("None"));
        messageBuilder.withTransactionCanceledException(exception);
        final String expected = String.format("%s. %s", MAIN_ERROR, SUB_ERROR_1);

        assertEquals(messageBuilder.build(), expected);
    }

    @Test
    public void GIVEN_mainError_and_subErrors_and_secondConditionFailed_WHEN_calling_build_THEN_return_expected_string() {
        final TransactionCanceledException exception = new TransactionCanceledException("test-message")
                .withCancellationReasons(
                        new CancellationReason().withCode("None"),
                        new CancellationReason().withCode("failed"));
        messageBuilder.withTransactionCanceledException(exception);
        final String expected = String.format("%s. %s", MAIN_ERROR, SUB_ERROR_2);

        assertEquals(messageBuilder.build(), expected);
    }

    @Test
    public void GIVEN_mainError_and_missing_subErrors_and_secondConditionFailed_WHEN_calling_build_THEN_return_expected_string() {
        final TransactionCanceledException exception = new TransactionCanceledException("test-message")
                .withCancellationReasons(
                        new CancellationReason().withCode("None"),
                        new CancellationReason().withCode("failed"));
        messageBuilder = new TransactionFailedMessageBuilder()
                .withMainError(MAIN_ERROR)
                .withSubError(SUB_ERROR_1)
                .withTransactionCanceledException(exception);
        final String expected = MAIN_ERROR + ". " +
                "Error with Condition #2 " +
                "(TransactionFailedMessageBuilder did not contain detailed error message)";

        assertEquals(messageBuilder.build(), expected);
    }
}
