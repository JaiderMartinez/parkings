package co.com.parking.r2dbc.dao;

import co.com.parking.r2dbc.entities.ParkingEntity;
import co.com.parking.r2dbc.utils.Constant;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

public interface ParkingDao extends R2dbcRepository<ParkingEntity, Long> {

    @Query(value = Constant.QUERY_FIND_PARKINGS_ORDER_BY_LOCATION_AND_WITH_LIMIT)
    Flux<ParkingEntity> findByLocation(@Param("latitudeInRadians") double latitudeInRadians,
                                       @Param("longitudeInRadians") double longitudeInRadians,
                                       @Param("limit") int limit);
}
