package co.com.parking.usecase;

import co.com.parking.model.parking.Parking;
import co.com.parking.model.parking.gateways.ParkingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ParkingUseCase {

    private final ParkingRepository parkingRepository;

    public ParkingUseCase(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    public Flux<Parking> findAll() {
        return parkingRepository.findAll();
    }

    // FIXME No permitir que nada sea null
    public Mono<Parking> save(Parking parking) {
        return parkingRepository.save(parking);
    }
}
