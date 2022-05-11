package mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException() {
        super("Incorrect password.");
    }
}