package co.com.parking.model.parking.gateways;

import co.com.parking.model.parking.ParkingSpace;
import reactor.core.publisher.Mono;

public interface ParkingSpaceRepository {

    Mono<ParkingSpace> findByIdParkingAndIdParkingSpace(Long idParking, Long idParkingSpace);

    Mono<ParkingSpace> save(ParkingSpace parkingSpace);

    Mono<ParkingSpace> findById(Long idParkingSpace);
}
