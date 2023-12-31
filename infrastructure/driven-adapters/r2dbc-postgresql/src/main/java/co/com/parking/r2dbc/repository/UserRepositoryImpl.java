package co.com.parking.r2dbc.repository;

import co.com.parking.model.parking.User;
import co.com.parking.model.parking.config.ErrorCode;
import co.com.parking.model.parking.config.ParkingException;
import co.com.parking.model.parking.gateways.UserGateway;
import co.com.parking.r2dbc.config.RetryConfig;
import co.com.parking.r2dbc.utils.Constant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Repository
public class UserRepositoryImpl implements UserGateway {

    @Qualifier(value = "webClientUser")
    private final WebClient webClient;
    private final RetryConfig retryConfig;

    public UserRepositoryImpl(WebClient webClient,
                              RetryConfig retryConfig) {
        this.webClient = webClient;
        this.retryConfig = retryConfig;
    }

    @Override
    public Mono<User> findById(Long idUser) {
        return webClient.get().uri(Constant.PATH_GET_USER_BY_ID, idUser.toString())
                .exchangeToMono(userResponse -> {
                    if (userResponse.statusCode().equals(HttpStatus.OK)) {
                        return userResponse.bodyToMono(User.class);
                    }
                    if (userResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.error(new ParkingException(ErrorCode.F404000));
                    }
                    if (userResponse.statusCode().equals(HttpStatus.NO_CONTENT)) {
                        return Mono.error(new ParkingException(ErrorCode.S204000));
                    }
                    return Mono.error(new ParkingException(ErrorCode.B500000));
                })
                .retryWhen(Retry.backoff(retryConfig.getMaxAttempts(), Duration.ofSeconds(retryConfig.getBackoffInterval())));
    }
}
