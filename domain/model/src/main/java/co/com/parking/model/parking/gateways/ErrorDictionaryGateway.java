package co.com.parking.model.parking.gateways;

import co.com.parking.model.parking.config.ErrorDictionary;
import reactor.core.publisher.Mono;

public interface ErrorDictionaryGateway {

    Mono<ErrorDictionary> findById(String id);
}
