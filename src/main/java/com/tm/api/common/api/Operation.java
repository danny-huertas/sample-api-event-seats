package com.tm.api.common.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

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

    public static Operation getSuccessOperation(String traceId, final Long startInstant) {
        return new ApiOperationBuilder().result(Result.OK).correlationId(traceId).errors(Collections.emptyList())
                .requestInstant(Instant.ofEpochMilli(startInstant).toString()).build();
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

    public static class ApiOperationBuilder {
        private Result builderResult;
        private String builderCorrelationId;
        private List<Error> builderErrors;
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
