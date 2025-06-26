package uz.oromland.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SanatoriumNotFountException extends RuntimeException {

    private final HttpStatus status;

    public SanatoriumNotFountException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }


}
