package co.com.parking.controllers.configuration.error;

import co.com.parking.controllers.dto.response.ResponseErrorDto;
import co.com.parking.controllers.mapper.ResponseErrorMapper;
import co.com.parking.controllers.utils.Constant;
import co.com.parking.model.parking.config.ErrorCode;
import co.com.parking.model.parking.config.ErrorDictionary;
import co.com.parking.model.parking.config.ParkingException;
import co.com.parking.usecase.ErrorDictionaryUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ErrorController implements ErrorWebExceptionHandler {

    private final ErrorDictionaryUseCase errorDictionaryUseCase;

    @ExceptionHandler(ParkingException.class)
    public Mono<ResponseEntity<ResponseErrorDto>> handlerParkingException(ParkingException e) {
        return genericHandleException(e.getError(), e);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ResponseErrorDto>> handleWebExchangeBindException(WebExchangeBindException e) {
        return genericHandleException(ErrorCode.B400000, e);
    }

    public Mono<ResponseEntity<ResponseErrorDto>> genericHandleException(ErrorCode errorCode, Exception e) {
        return errorDictionaryUseCase.findById(errorCode.getCode())
                .defaultIfEmpty(getDefaultErrorDictionary())
                .map(errorDictionary -> getErrorEntity(errorDictionary, e));
    }

    private ErrorDictionary getDefaultErrorDictionary() {
        return ErrorDictionary.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .messageEs(Constant.MESSAGE_ES_ERROR_DEFAULT)
                .messageEn(Constant.MESSAGE_EN_ERROR_DEFAULT)
                .id("B500-000")
                .build();
    }

    private ResponseEntity<ResponseErrorDto> getErrorEntity(ErrorDictionary errorDictionary, Exception e) {
        return ResponseEntity.status(errorDictionary.getHttpStatus())
                .headers(getHeaders(e.getMessage()))
                .body(ResponseErrorMapper.toResponseErrorDto(errorDictionary));
    }

    private HttpHeaders getHeaders(String message) {
        log.error(Constant.LOG_CONTROLLER_ADVICE, message);
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constant.KEY_HEADER_MESSAGE, message);
        return headers;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        exchange.getResponse().getHeaders().set(Constant.KEY_HEADER_MESSAGE, ex.getMessage());
        if (ex instanceof ParkingException parkingEx) {
            return genericHandleException(parkingEx.getError(), parkingEx)
                    .flatMap(errorResponse -> {
                        exchange.getResponse().setStatusCode(errorResponse.getStatusCode());
                        return exchange
                                .getResponse()
                                .writeWith(get(errorResponse.getBody(), exchange));
                    });
        }
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return exchange
                .getResponse()
                .writeWith(
                        get(ResponseErrorMapper
                                .toResponseErrorDto(getDefaultErrorDictionary()), exchange)
                );
    }

    private Flux<DataBuffer> get(ResponseErrorDto responseErrorDto, ServerWebExchange exchange) {
        return DataBufferUtils.read(
                new ByteArrayResource(getBody(responseErrorDto)),
                exchange.getResponse().bufferFactory(),
                4096
        );
    }

    private byte[] getBody(ResponseErrorDto responseErrorDto)  {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsBytes(responseErrorDto);
        } catch (JsonProcessingException e) {
            return Constant.BODY_DEFAULT.getBytes();
        }
    }
}
