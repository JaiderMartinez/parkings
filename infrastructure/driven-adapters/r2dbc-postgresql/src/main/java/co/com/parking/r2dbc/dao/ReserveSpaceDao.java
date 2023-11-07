package co.com.parking.r2dbc.dao;

import co.com.parking.r2dbc.entities.ReserveSpaceEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ReserveSpaceDao extends R2dbcRepository<ReserveSpaceEntity, Long> {

    Mono<ReserveSpaceEntity> findByParkingSpaceEntityIdAndIdUserAndReservationEndDateIsNull(Long idParking, Long idUser);
}
