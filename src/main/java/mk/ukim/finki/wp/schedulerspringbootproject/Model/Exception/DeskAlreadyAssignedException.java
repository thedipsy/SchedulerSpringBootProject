package mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception;

public class DeskAlreadyAssignedException extends RuntimeException {
    public DeskAlreadyAssignedException(int ordinalNumber) {
        super(String.format("Desk %d is already assigned to employee.", ordinalNumber));
    }
}
