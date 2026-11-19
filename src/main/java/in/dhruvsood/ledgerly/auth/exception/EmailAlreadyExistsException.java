package in.dhruvsood.ledgerly.auth.exception;


public class EmailAlreadyExistsException extends  RuntimeException{

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
