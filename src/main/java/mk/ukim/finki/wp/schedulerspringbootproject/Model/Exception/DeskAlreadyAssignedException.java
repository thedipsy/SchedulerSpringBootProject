package mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class DeskAlreadyAssignedException extends RuntimeException {
    public DeskAlreadyAssignedException(int ordinalNumber) {
        super(String.format("Desk %d is already assigned to employee.", ordinalNumber));
    }
}
