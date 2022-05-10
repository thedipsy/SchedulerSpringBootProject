package mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidCompanyKeyException extends RuntimeException{
    public InvalidCompanyKeyException() {
        super("Invalid key.");
    }
}

