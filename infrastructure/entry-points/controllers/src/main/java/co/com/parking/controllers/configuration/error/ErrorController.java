package co.com.parking.controllers.configuration.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

@ControllerAdvice
public class ErrorController {
    private static final ConcurrentHashMap<String, Error> STATUS_CODES = new ConcurrentHashMap<>();

    private ErrorController() {
        STATUS_CODES.put(NotFoundException.class.getSimpleName(),
                new Error(ErrorCodeEnum.NOT_FOUND.getCode(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler({Exception.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e, HandlerMethod handlerMethod) {
        ErrorResponse errorResponse = new ErrorResponse();
        String exceptionName = e.getClass().getSimpleName();
        Error error = STATUS_CODES.get(exceptionName);
        if (error == null) {
            error = new Error("500", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        errorResponse.setExceptionName(exceptionName);
        errorResponse.setMethodName(handlerMethod.getMethod().getName());
        errorResponse.setParameters(Arrays.toString(handlerMethod.getMethodParameters()));
        errorResponse.setControllerErrorOrigin(handlerMethod.getMethod().getDeclaringClass().getSimpleName());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setStatusCode(error.getStatus());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(error.getStatus()));
    }
}
