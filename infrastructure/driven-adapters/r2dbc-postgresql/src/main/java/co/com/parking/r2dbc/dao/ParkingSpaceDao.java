package co.com.parking.r2dbc.dao;

import co.com.parking.r2dbc.entities.ParkingSpaceEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

public interface ParkingSpaceDao extends R2dbcRepository<ParkingSpaceEntity, Long> {

    @Query( "SELECT * FROM parking_spaces " +
            "INNER JOIN parkings " +
            "ON parking_spaces.id = parkings.id " +
            "WHERE parking_spaces.id = :id_parking_space AND parkings.id = :id_parking")
    Mono<ParkingSpaceEntity> findByIdAndParkingEntityId(@Param("id_parking_space") Long idParkingSpace, @Param("id_parking") Long idParking);
}
