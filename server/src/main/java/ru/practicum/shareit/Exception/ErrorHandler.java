package ru.practicum.shareit.Exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(final IllegalArgumentException e) {
        log.debug(e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(DublicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDublicate(final DublicateException e) {
        log.debug(e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundExeption(final NotFoundException e) {
        log.debug(e.getMessage());
        return Map.of("error", e.getMessage());
    }
}
