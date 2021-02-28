package com.nent.techtest.model;




public enum MessageCodes {
    INVALID  (501),
    NOT_FOUND(500),
    OK   (200)
    ;


    private final int code;

    private MessageCodes(int code) {
        this.code = code;
    }
}