package ru.practicum.shareit.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.common.ecxeption.AccessDenyException;
import ru.practicum.shareit.common.ecxeption.ErrorMessage;
import ru.practicum.shareit.common.ecxeption.ItemNotAvailbaleException;
import ru.practicum.shareit.common.ecxeption.NotFoundException;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFound(final RuntimeException e) {
        log.error(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler({AccessDenyException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleAccessDenyException(final Exception e) {
        log.error(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler({ItemNotAvailbaleException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleItemNotAvailbaleException(final Exception e) {
        log.error(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleIllegalArgumentException(final Exception e) {
        log.error(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleOtherExceptions(final Exception e) {
        log.error(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }
}
