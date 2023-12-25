package co.com.parking.model.parking.gateways;

import co.com.parking.model.parking.ReserveSpace;
import reactor.core.publisher.Mono;

public interface ReserveSpaceInParkingRepository {

    Mono<ReserveSpace> save(ReserveSpace reserveSpace);

    Mono<ReserveSpace> findOpenReservedSpace(Long idParking, Long idParkingSpace, Long idUser);
}
