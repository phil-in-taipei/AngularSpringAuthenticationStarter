package backend.security.exceptions.user;

import backend.security.models.user.User;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {}
}
