package uz.oromland.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException e) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Token muddati tugagan");
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<?> handleMalformedJwtException(MalformedJwtException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Token format noto‘g‘ri");
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> handleSignatureException(SignatureException e) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Token imzosi noto‘g‘ri");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException e) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Login yoki parol noto‘g‘ri");
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Noma’lum server xatosi: " + e.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}
