package co.com.pragma.model.solicitudes.exception;

import javax.naming.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {


    public InvalidTokenException(String msg) {
        super(msg);
    }

}
