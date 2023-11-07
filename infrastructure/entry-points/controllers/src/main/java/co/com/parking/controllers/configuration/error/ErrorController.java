package co.com.parking.controllers.configuration.error;

import co.com.parking.usecase.exceptions.FailedTakeParkingSpaceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ControllerAdvice
public class ErrorController {
    private static final ConcurrentHashMap<String, Error> STATUS_CODES = new ConcurrentHashMap<>();

    private ErrorController() {
        STATUS_CODES.put(NotFoundException.class.getSimpleName(),
                new Error(ErrorCodeEnum.NOT_FOUND.getCode(), HttpStatus.NO_CONTENT.value()));
        STATUS_CODES.put(FailedTakeParkingSpaceException.class.getSimpleName(),
                new Error());
    }

    @ExceptionHandler({Exception.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e, HandlerMethod handlerMethod) {
        ErrorResponse errorResponse = new ErrorResponse();
        String exceptionName = e.getClass().getSimpleName();
        Error error = STATUS_CODES.get(exceptionName);
        if (error == null) {
            error = new Error("500", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        //TODO Quiero guardar estos errores en cloudWatch
        errorResponse.setExceptionName(exceptionName);
        errorResponse.setMethodName(handlerMethod.getMethod().getName());
        errorResponse.setDescription(getDescriptionOfTheException(e));
        errorResponse.setControllerErrorOrigin(handlerMethod.getMethod().getDeclaringClass().getSimpleName());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setStatusCode(error.getStatus());
        log.info("Error message : {} class name : {}", e.getMessage(), e.getClass().getSimpleName());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(error.getStatus()));
    }

    private String getDescriptionOfTheException(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder descriptionBuilder = new StringBuilder();
        Arrays.stream(stackTrace).forEach( stackTraceElement -> {
            descriptionBuilder.append("Class ").append(stackTraceElement.getClassName());
            descriptionBuilder.append(" Method ").append(stackTraceElement.getMethodName());
            descriptionBuilder.append(" Line number ").append(stackTraceElement.getLineNumber());

        });
        return descriptionBuilder.toString();
    }
}
