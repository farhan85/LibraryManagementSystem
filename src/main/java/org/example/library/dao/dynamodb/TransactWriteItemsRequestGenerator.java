package org.example.library.dao.dynamodb;

import com.amazonaws.services.dynamodbv2.model.TransactWriteItemsRequest;
import com.amazonaws.services.dynamodbv2.model.TransactionCanceledException;

/**
 * Implementations of this class will return an ordered list of TransactWriteItems
 * and a corresponding error message to return if a TransactionCanceledException
 * is thrown.
 */
public interface TransactWriteItemsRequestGenerator<R> {

    TransactWriteItemsRequest createRequest(R resource);

    String createTransactionCanceledExceptionMessage(TransactionCanceledException e, R resource);
}
