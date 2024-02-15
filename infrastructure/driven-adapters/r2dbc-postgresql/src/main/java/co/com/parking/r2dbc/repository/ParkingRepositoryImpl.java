package co.com.parking.r2dbc.repository;

import co.com.parking.model.parking.Parking;
import co.com.parking.model.parking.gateways.ParkingRepository;
import co.com.parking.r2dbc.dao.ParkingDao;
import co.com.parking.r2dbc.mapper.ParkingMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ParkingRepositoryImpl implements ParkingRepository {

    private final ParkingDao parkingDao;
    private final String limitParkingLotsByLocation;

    public ParkingRepositoryImpl(@Value("${variable.limit.parkings}")
                                 String limitParkingLotsByLocation,
                                 ParkingDao parkingDao) {
        this.parkingDao = parkingDao;
        this.limitParkingLotsByLocation = limitParkingLotsByLocation;
    }

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

    @Override
    public Flux<Parking> findByLocation(double latitudeInRadians, double longitudeInRadians) {
        return parkingDao.findByLocation(latitudeInRadians, longitudeInRadians, Integer.parseInt(limitParkingLotsByLocation))
                .map(ParkingMapper::toModel);
    }
}
