package co.com.parking.r2dbc.repository;

import co.com.parking.model.parking.ReserveSpace;
import co.com.parking.model.parking.gateways.ReserveSpaceInParkingRepository;
import co.com.parking.r2dbc.dao.ReserveSpaceDao;
import co.com.parking.r2dbc.mapper.ReserveSpaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ReserveSpaceInParkingRepositoryImpl implements ReserveSpaceInParkingRepository {

    private final ReserveSpaceDao reserveSpaceDao;

    @Override
    public Mono<ReserveSpace> save(ReserveSpace reserveSpace) {
        return reserveSpaceDao.save(ReserveSpaceMapper.toEntity(reserveSpace))
                .map(ReserveSpaceMapper::toModel);
    }

    @Override
    public Mono<ReserveSpace> findOpenReservedSpace(Long idParking, Long idParkingSpace, Long idUser) {
        return reserveSpaceDao.findOpenReservedSpace(idParking, idParkingSpace, idUser)
                .map(ReserveSpaceMapper::toModel);
    }
}
