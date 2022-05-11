package mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException() {
        super("Invalid credentials.");
    }
}

