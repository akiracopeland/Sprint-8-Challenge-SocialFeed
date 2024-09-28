package com.bloomtech.socialfeed.validators;

import com.bloomtech.socialfeed.exceptions.UserValidationException;
import com.bloomtech.socialfeed.models.Role;
import com.bloomtech.socialfeed.models.User;

public class UserInfoValidator implements Validator {

    private boolean isValidUsername(String username) {
        String regex = "^[A-Z][a-zA-Z0-9]{3,}$";

        return username.matches(regex);

    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d!@#$%^&*]{8,}$";

        return password.matches(regex);
    }

    @Override
    public void validate(Object userData) {

        User user = (User) userData;

        if (!isValidUsername(user.getUsername())) {
            throw new UserValidationException("Invalid Username: Username must be at least 4 characters long, " +
                    "must begin with an uppercase letter, and may not contain special characters or spaces!");
        }
        if (!isValidPassword(user.getPassword())) {
            throw new UserValidationException("Invalid Password: Password must be at least 8 characters long, " +
                    "contain at least one uppercase letter, one lowercase letter, and one special character!");
        }
        if (user.getRole() == null) { user.setRole(Role.USER); }
    }
}
