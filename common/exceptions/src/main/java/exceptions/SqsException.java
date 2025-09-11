package exceptions;

public class SqsException extends  RuntimeException {
    public SqsException(String message) {
        super(message);
    }
}
