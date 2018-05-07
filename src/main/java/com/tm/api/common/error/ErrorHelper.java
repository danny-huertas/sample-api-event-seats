package com.tm.api.common.error;

import com.tm.api.common.api.Error;
import com.tm.api.common.api.Operation;
import com.tm.api.common.api.OperationError;
import com.tm.api.common.api.Result;
import com.tm.api.common.localization.MessageHandler;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is uses error codes to build an error response
 */
@Component
public class ErrorHelper {
    private MessageHandler messageHandler;

    @Autowired
    public ErrorHelper(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * This method is used to build an array of errors that is returned in the operation body response
     *
     * @param errors List of errors that were detected during api. The
     *         format of these errors is {code}-{field}-{message}
     * @param traceId traceId of the request
     * @return Error bean to be converted to error response
     */
    public OperationError errorResponse(List<String> errors, String traceId) {
        final List<Error> errorList = new ArrayList<>();

        //add each error to the error list
        errors.forEach(error -> {
            final String[] errorSplit = error.split(ErrorConstants.ERROR_SPLITTER);

            // before setting the code / field, validate the error split array is greater than zero
            Validate.isTrue(errorSplit.length > 0,
                    messageHandler.localizeMessage(ErrorConstants.ERROR_FORMAT_NOT_SUPPORTED) + error);
            String code = errorSplit[0];
            String field = errorSplit[1];
            String message = messageHandler.localizeMessage(code);

            //add the error to the error list
            errorList.add(new Error.ApiErrorBuilder().errorCode(code).errorMessage(message).moreInfo(field).build());
        });

        //build and return the operation error object
        return new OperationError(
                new Operation.ApiOperationBuilder().result(Result.ERROR).correlationId(traceId).errors(errorList)
                        .requestInstant(null).build());
    }

    /**
     * This method is used to set the operation body response with the single error.
     *
     * @param errorCode code of error that was detected
     * @param traceId traceId of the request
     * @param errorMessage error message
     * @param moreInfo more info field
     * @param startTime start time of the api
     * @return Error bean to be converted to error response
     */
    public OperationError errorResponse(final String errorCode, final String traceId, final String errorMessage,
            final String moreInfo, final Object startTime) {
        //build and return the operation error object
        return new OperationError(new Operation.ApiOperationBuilder().result(Result.ERROR).correlationId(traceId)
                .errors(Collections.singletonList(
                        new Error.ApiErrorBuilder().errorCode(errorCode).errorMessage(errorMessage).moreInfo(moreInfo)
                                .build())).requestInstant(startTime).build());
    }
}
