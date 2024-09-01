package banquemisr.challenge05.taskmanagementservice.exception;

import banquemisr.challenge05.taskmanagementservice.web.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.webjars.NotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
//@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> notFoundExceptionHandler(NotFoundException ex, WebRequest request) {
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

    @ExceptionHandler(InvalidDueDateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<?> handleInvalidDueDateException(InvalidDueDateException exception) {
        return ApiResponse.builder()
                .message(exception.getMessage())
                .statusCode(HttpStatus.CONFLICT.value())
                .build();
    }

    }
