package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.error.model.ErrorMessage;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleBadRequest(MethodArgumentNotValidException e) {
        String field = Objects.requireNonNull(e.getBindingResult().getFieldError()).getField();
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String message = String.format("Поле %s %s", field, errorMessage);

        log.error(message);

        return new ErrorMessage(message);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleConstraintViolationException(final Exception e) {
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
