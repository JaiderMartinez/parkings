package co.com.parking.r2dbc.dao;

import co.com.parking.r2dbc.entities.ReserveSpaceEntity;
import co.com.parking.r2dbc.utils.Constant;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

public interface ReserveSpaceDao extends R2dbcRepository<ReserveSpaceEntity, Long> {

    @Query(Constant.QUERY_FIND_OPEN_RESERVED_SPACE_IN_A_PARKING)
    Mono<ReserveSpaceEntity> findOpenReservedSpace(@Param("idParking") Long idParking,
                                                   @Param("idParkingSpace") Long idParkingSpace,
                                                   @Param("idUser") Long idUser);
}
