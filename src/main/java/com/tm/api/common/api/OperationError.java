package com.tm.api.common.api;

/**
 * This is a wrapper class to retain the JSON root structure 'operation'
 */
public class OperationError {

    private Operation operation;

    public OperationError(Operation operation) {
        this.operation = operation;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
