package co.com.parking.model.parking.gateways;

import co.com.parking.model.parking.User;
import reactor.core.publisher.Mono;

public interface UserGateway {

    Mono<User> findById(Long idUser);
}
