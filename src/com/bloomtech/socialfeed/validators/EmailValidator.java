package com.bloomtech.socialfeed.validators;

import com.bloomtech.socialfeed.exceptions.EmailValidationException;

public class EmailValidator implements Validator {
    public EmailValidator() {
    }

    private boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_.]+@[a-zA-Z]+\\.[a-zA-Z]+$";

        return email.matches(regex);
    }

    @Override
    public void validate(Object emailData) {
        String email = (String) emailData;
        if (!isValidEmail(email)) {
            throw new EmailValidationException("Invalid Email: Email address must include '@' before domain and a domain identifier after a '.'!");
        }
    }
}
