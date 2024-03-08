package cryptomessenger.server.infrastructure.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler
    public String handle(Exception exception) {
        return exception.getMessage();
    }
}
