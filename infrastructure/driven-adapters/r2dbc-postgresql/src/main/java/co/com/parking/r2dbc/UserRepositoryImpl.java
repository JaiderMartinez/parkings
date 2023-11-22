package co.com.parking.r2dbc;

import co.com.parking.model.parking.User;
import co.com.parking.model.parking.gateways.UserGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryImpl implements UserGateway {

    @Qualifier(value = "webClientUser")
    private final WebClient webClient;

    public UserRepositoryImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<User> findById(Long idUser) {
        return webClient.get().uri("user/{idUser}", idUser.toString())
                .exchangeToMono(userResponse ->
                     userResponse.bodyToMono(User.class)
                );
    }
}
