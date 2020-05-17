package tqs.group4.bestofbooks.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NullBookException extends RuntimeException {
    public NullBookException(String message) {
        super(message);
    }

    public NullBookException() {
    }
}