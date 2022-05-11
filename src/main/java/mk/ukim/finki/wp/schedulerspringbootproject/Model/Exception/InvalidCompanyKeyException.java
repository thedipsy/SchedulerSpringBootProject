package mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception;

public class InvalidCompanyKeyException extends RuntimeException{
    public InvalidCompanyKeyException() {
        super("Invalid key.");
    }
}

