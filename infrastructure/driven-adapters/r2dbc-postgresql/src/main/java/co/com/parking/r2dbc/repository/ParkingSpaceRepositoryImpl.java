package co.com.parking.r2dbc.repository;

import co.com.parking.model.parking.ParkingSpace;
import co.com.parking.model.parking.gateways.ParkingSpaceRepository;
import co.com.parking.r2dbc.dao.ParkingSpaceDao;
import co.com.parking.r2dbc.mapper.ParkingSpaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ParkingSpaceRepositoryImpl implements ParkingSpaceRepository {

    private final ParkingSpaceDao parkingSpaceDao;

    @Override
    public Mono<ParkingSpace> findByIdParkingAndIdParkingSpace(Long idParking, Long idParkingSpace) {
        return parkingSpaceDao.findByIdAndParkingEntityId(idParkingSpace, idParking)
                .map(ParkingSpaceMapper::toModel);
    }

    @Override
    public Mono<ParkingSpace> save(ParkingSpace parkingSpace) {
        return parkingSpaceDao.save( ParkingSpaceMapper.toEntity(parkingSpace) )
                .map(ParkingSpaceMapper::toModel);
    }

    @Override
    public Mono<ParkingSpace> findById(Long idParkingSpace) {
        return parkingSpaceDao.findById(idParkingSpace)
                .map(ParkingSpaceMapper::toModel);
    }
}
