package mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception;

public class PasswordsDoNotMatchException extends RuntimeException{
    public PasswordsDoNotMatchException() {
        super("Passwords do not match.");
    }
}
