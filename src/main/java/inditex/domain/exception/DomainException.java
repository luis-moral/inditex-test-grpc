package inditex.domain.exception;

public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(Exception exception) {
        super(exception);
    }
}
