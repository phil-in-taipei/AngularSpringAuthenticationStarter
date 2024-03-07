package backend.security.exceptions.user;

public class SaveEditedUserException extends Exception {
    public SaveEditedUserException(String message) {
        super(message);
    }

    public SaveEditedUserException() {}
}
