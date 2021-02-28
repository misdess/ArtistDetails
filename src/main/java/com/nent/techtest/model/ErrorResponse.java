package com.nent.techtest.model;

public class ErrorResponse {

    MessageCodes code;
    String message;

    public MessageCodes getCode() {
        return code;
    }

    public void setCode(MessageCodes code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorResponse(MessageCodes code, String message) {
        this.code=code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "code:'" + code.name() + '\'' +
                ", message:'" + message + '\'' +
                '}';
    }
}
