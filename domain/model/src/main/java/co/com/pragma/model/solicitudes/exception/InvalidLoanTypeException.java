package co.com.pragma.model.solicitudes.exception;

public class InvalidLoanTypeException extends RuntimeException {
    public InvalidLoanTypeException(String message) {
        super(message);
    }
}
