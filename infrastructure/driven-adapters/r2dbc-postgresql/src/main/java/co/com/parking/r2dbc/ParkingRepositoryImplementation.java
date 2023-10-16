package co.com.parking.r2dbc;

import co.com.parking.r2dbc.dao.ParkingDao;
import co.com.parking.r2dbc.entities.ParkingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class ParkingRepositoryImplementation {

    private final ParkingDao parkingDao;

    public Flux<ParkingEntity> findAll() {
        return parkingDao.findAll();
    }
}
