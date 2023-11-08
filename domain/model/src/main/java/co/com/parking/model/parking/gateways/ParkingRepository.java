package co.com.parking.model.parking.gateways;

import co.com.parking.model.parking.Parking;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ParkingRepository {

    Mono<Parking> save(Parking parking);

    Flux<Parking> findAll();

    Mono<Parking> findById(Long idParking);
}
