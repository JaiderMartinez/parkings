package co.com.parking.r2dbc.dao;

import co.com.parking.r2dbc.entities.ReserveSpaceEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

public interface ReserveSpaceDao extends R2dbcRepository<ReserveSpaceEntity, Long> {

    @Query( "SELECT * FROM reserve_space " +
            "INNER JOIN parking_spaces " +
            "ON reserve_space.id_parking_space = parking_spaces.id " +
            "INNER JOIN parkings " +
            "ON parking_spaces.id_parking = parkings.id " +
            "WHERE reserve_space.id_user = :idUser " +
            "AND parkings.id = :idParking " +
            "AND reserve_space.reservation_end_date IS NULL")
    Mono<ReserveSpaceEntity> findByIdParkingAndIdUserAndReservationEndDateIsNull(@Param("idParking") Long idParking, @Param("idUser") Long idUser);
}
