package co.com.parking.usecase;

import co.com.parking.model.parking.config.ErrorDictionary;
import co.com.parking.model.parking.gateways.ErrorDictionaryGateway;
import reactor.core.publisher.Mono;

public class ErrorDictionaryUseCase {

    private final ErrorDictionaryGateway errorDictionaryGateway;

    public ErrorDictionaryUseCase(ErrorDictionaryGateway errorDictionaryGateway) {
        this.errorDictionaryGateway = errorDictionaryGateway;
    }

    public Mono<ErrorDictionary> findById(String id) {
        return errorDictionaryGateway.findById(id);
    }
}
