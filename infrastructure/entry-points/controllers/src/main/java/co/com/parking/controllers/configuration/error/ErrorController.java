package co.com.parking.controllers.configuration.error;

import co.com.parking.controllers.dto.response.ResponseErrorDto;
import co.com.parking.controllers.mapper.ResponseErrorMapper;
import co.com.parking.model.parking.config.ErrorCode;
import co.com.parking.model.parking.config.ErrorDictionary;
import co.com.parking.model.parking.config.ParkingException;
import co.com.parking.usecase.ErrorDictionaryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ErrorController {

    private final ErrorDictionaryUseCase errorDictionaryUseCase;

    @ExceptionHandler({Exception.class, ParkingException.class})
    public Mono<ResponseEntity<ResponseErrorDto>> handlerParkingException(ParkingException e) {
        return genericHandleException(e.getError(), e);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ResponseErrorDto>> handleWebExchangeBindException(WebExchangeBindException e) {
        return genericHandleException(ErrorCode.B400000, e);
    }

    public Mono<ResponseEntity<ResponseErrorDto>> genericHandleException(ErrorCode errorCode, Exception e) {
        return errorDictionaryUseCase.findById(errorCode.getCode())
                .defaultIfEmpty(ErrorDictionary.builder()
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .messageEs("Ha ocurrido un error en el sistema, por favor contacte al administrador")
                        .messageEn("An error has occurred in the system, please contact the administrator")
                        .id(ErrorCode.B500000.getCode())
                        .build())
                .map(errorDictionary -> getErrorEntity(errorDictionary, e));
    }

    private ResponseEntity<ResponseErrorDto> getErrorEntity(ErrorDictionary errorDictionary, Exception e) {
        return ResponseEntity.status(errorDictionary.getHttpStatus())
                .headers(getHeaders(e.getMessage()))
                .body(ResponseErrorMapper.toResponseErrorDto(errorDictionary));
    }

    private HttpHeaders getHeaders(String message) {
        log.error("Error message : {}", message);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", message);
        return headers;
    }
}
