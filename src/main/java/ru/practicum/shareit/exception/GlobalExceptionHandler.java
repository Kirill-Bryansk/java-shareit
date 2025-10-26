package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        log.error("Ошибка валидации полей объекта: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class, ForbiddenAccessException.class})
    public ResponseEntity<?> handleExceptions(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        if (ex instanceof ForbiddenAccessException) {
            errorResponse.put("message", "Ошибка при выполнении запроса сервером: " + ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        } else {
            errorResponse.put("message", "Ошибка при выполнении запроса сервером: " + ex.getMessage());
            log.error(errorResponse.get("message"));
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "Ошибка при чтении тела запроса: " + ex.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<?> handlerHttpClientErrorException(HttpClientErrorException.NotFound ex) {
        String errorMessage = "Ошибка поступивших на сервер данных: " + ex.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
        String errorMessage = "Ошибка: " + ex.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex) {
        String errorMessage = "Ошибка валидации: " + ex.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
