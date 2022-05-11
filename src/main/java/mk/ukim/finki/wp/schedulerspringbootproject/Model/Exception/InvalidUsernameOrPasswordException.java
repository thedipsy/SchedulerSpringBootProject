package mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception;

public class InvalidUsernameOrPasswordException extends RuntimeException{
    public InvalidUsernameOrPasswordException() {
        super("Invalid email or password.");
    }
}
