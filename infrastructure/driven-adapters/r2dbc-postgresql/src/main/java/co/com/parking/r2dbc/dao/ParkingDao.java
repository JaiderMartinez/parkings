package co.com.parking.r2dbc.dao;

import co.com.parking.r2dbc.entities.ParkingEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ParkingDao extends R2dbcRepository<ParkingEntity, Long> {
}
