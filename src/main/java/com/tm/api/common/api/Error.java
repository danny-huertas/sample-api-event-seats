package com.tm.api.common.api;

/**
 * Error Object (uses the builder pattern)
 */
public class Error {
    private String errorCode;
    private String errorMessage;
    private String moreInfo;

    private Error(ApiErrorBuilder apiErrorBuilder) {
        this.errorCode = apiErrorBuilder.builderErrorCode;
        this.errorMessage = apiErrorBuilder.builderErrorMessage;
        this.moreInfo = apiErrorBuilder.builderMoreInfo;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    /**
     * ApiErrorBuilder Object
     */
    public static class ApiErrorBuilder {
        private String builderErrorCode;
        private String builderErrorMessage;
        private String builderMoreInfo;

        public ApiErrorBuilder errorCode(String errorCode) {
            this.builderErrorCode = errorCode;
            return this;
        }

        public ApiErrorBuilder errorMessage(String errorMessage) {
            this.builderErrorMessage = errorMessage;
            return this;
        }

        public ApiErrorBuilder moreInfo(String moreInfo) {
            this.builderMoreInfo = moreInfo;
            return this;
        }

        public Error build() {
            return new Error(this);
        }
    }
}
