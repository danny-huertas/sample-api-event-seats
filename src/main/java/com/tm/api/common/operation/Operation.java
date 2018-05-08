package com.tm.api.common.operation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Operation Object (uses the builder pattern)
 */
@JsonDeserialize(builder = Operation.ApiOperationBuilder.class)
public class Operation {
    private String result;
    private String correlationId;
    private List<Error> errors;
    private String requestTimeStampUtc;

    private Operation(ApiOperationBuilder apiOperationBuilder) {
        this.result = apiOperationBuilder.builderResult.name();
        this.correlationId = apiOperationBuilder.builderCorrelationId;
        this.errors = apiOperationBuilder.builderErrors;
        this.requestTimeStampUtc = null != apiOperationBuilder.builderRequestInstant ?
                apiOperationBuilder.builderRequestInstant.toString() :
                Instant.now().toString();
    }

    public String getResult() {
        return result;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public String getRequestTimeStampUtc() {
        return requestTimeStampUtc;
    }

    /**
     * ApiOperationBuilder Object
     */
    public static class ApiOperationBuilder {
        private Result builderResult;
        private String builderCorrelationId;
        private List<Error> builderErrors = new ArrayList<>();
        private Object builderRequestInstant;

        public ApiOperationBuilder result(Result result) {
            this.builderResult = result;
            return this;
        }

        public ApiOperationBuilder correlationId(String correlationId) {
            this.builderCorrelationId = correlationId;
            return this;
        }

        public ApiOperationBuilder errors(List<Error> errors) {
            this.builderErrors = errors;
            return this;
        }

        public ApiOperationBuilder requestInstant(Object requestInstant) {
            this.builderRequestInstant = requestInstant;
            return this;
        }

        public Operation build() {
            return new Operation(this);
        }
    }
}
