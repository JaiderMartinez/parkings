package co.com.parking.r2dbc;

import co.com.parking.model.parking.Parking;
import co.com.parking.model.parking.gateways.ParkingRepository;
import co.com.parking.r2dbc.dao.ParkingDao;
import co.com.parking.r2dbc.mapper.ParkingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ParkingRepositoryImpl implements ParkingRepository {

    private final ParkingDao parkingDao;

    @Override
    public Mono<Parking> save(Parking parking) {
        return parkingDao.save(ParkingMapper.toEntity(parking))
                .map(ParkingMapper::toModel);
    }

    public Flux<Parking> findAll() {
        return parkingDao.findAll()
                .map(ParkingMapper::toModel);
    }

    @Override
    public Mono<Parking> findById(Long idParking) {
        return parkingDao.findById(idParking)
                .map(ParkingMapper::toModel);
    }
}
