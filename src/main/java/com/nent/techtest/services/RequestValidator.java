package com.nent.techtest.services;

import org.springframework.stereotype.Component;

@Component
public class RequestValidator {

    public RequestValidator() {
    }

    public boolean validateMdIdLength(String mdId) {
        return mdId.length() == 36;
    }

    //Other validations
}
