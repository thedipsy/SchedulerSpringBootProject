package mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception;

public class UniqueOrdinalNumberException extends RuntimeException {
    public UniqueOrdinalNumberException(){super("Ordinal number must be unique.");}
}
