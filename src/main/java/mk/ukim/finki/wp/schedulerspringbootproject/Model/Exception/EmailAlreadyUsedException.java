package mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super(String.format("Email %s is already is use.", email));
    }
}

