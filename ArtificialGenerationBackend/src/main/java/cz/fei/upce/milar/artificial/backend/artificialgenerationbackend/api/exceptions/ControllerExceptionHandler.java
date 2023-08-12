package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(value = {IOException.class})
    protected ResponseEntity<Object> handleIOException(IOException ex, WebRequest request) {
        logger.error(ex.getMessage());

        return handleExceptionInternal(ex, "File not found",
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<Object> handleIOException(RuntimeException ex, WebRequest request) {
        logger.error(ex.getMessage());

        return handleExceptionInternal(ex, "Runtime error occured",
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleIOException(Exception ex, WebRequest request) {
        logger.error(ex.getMessage());

        return handleExceptionInternal(ex, "Error occured",
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
