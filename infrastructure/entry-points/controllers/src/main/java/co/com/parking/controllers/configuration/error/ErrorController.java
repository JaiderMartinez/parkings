package co.com.parking.controllers.configuration.error;

import co.com.parking.controllers.dto.response.ResponseErrorDto;
import co.com.parking.controllers.mapper.ResponseErrorMapper;
import co.com.parking.model.parking.config.ErrorCode;
import co.com.parking.model.parking.config.ErrorDictionary;
import co.com.parking.usecase.ErrorDictionaryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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

    @ExceptionHandler({WebExchangeBindException.class})
    public Mono<ResponseEntity<ResponseErrorDto>> handleWebExchangeBindException(WebExchangeBindException e) {
        return genericHandleException(ErrorCode.B400000, e);
    }

    public Mono<ResponseEntity<ResponseErrorDto>> genericHandleException(ErrorCode errorCode, Exception e) {
        return errorDictionaryUseCase.findById(errorCode.getCode())
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
