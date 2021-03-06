package mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OfficeNotFoundException extends RuntimeException{
    public OfficeNotFoundException() {
        super("Office was not found.");
    }
}
