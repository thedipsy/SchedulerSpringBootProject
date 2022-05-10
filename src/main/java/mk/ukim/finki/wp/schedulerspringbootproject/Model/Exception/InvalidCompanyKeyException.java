package mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidCompanyKeyException extends RuntimeException{
    public InvalidCompanyKeyException() {
        super("Invalid key.");
    }
}

