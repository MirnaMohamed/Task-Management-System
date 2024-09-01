package banquemisr.challenge05.taskmanagementservice.exception;

import banquemisr.challenge05.taskmanagementservice.web.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
//@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> notFoundExceptionHandler(NotFoundException ex) {
        return ApiResponse.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiResponse<?> handleInvalidArgument(MethodArgumentNotValidException exception) {
        Map<String, String> map =  new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError ->
                map.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return ApiResponse.builder()
                .message(exception.getTitleMessageCode())
                .data(map)
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiResponse<?> handleNotReadableException(HttpMessageNotReadableException exception) {
        return ApiResponse.builder()
                .message(exception.getMessage())
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .build();
    }

    @ExceptionHandler(value = {InvalidDueDateException.class, UserAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<?> handleInvalidDueDateException(RuntimeException exception) {
        return ApiResponse.builder()
                .message(exception.getMessage())
                .statusCode(HttpStatus.CONFLICT.value())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleConstraintViolationException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        StringBuilder builder = new StringBuilder();
        violations.forEach((violation) ->
                builder.append(violation.getMessageTemplate()));
        return ApiResponse.builder()
                .message("Validation failed: " + builder)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
    }

}