package co.com.parking.r2dbc.repository;

import co.com.parking.r2dbc.dao.ParkingSpaceDao;
import co.com.parking.r2dbc.entities.ParkingSpaceEntity;
import co.com.parking.r2dbc.mapper.ParkingSpaceMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingSpaceRepositoryImplTest {

    @Mock
    private ParkingSpaceDao parkingSpaceDao;
    @InjectMocks
    private ParkingSpaceRepositoryImpl parkingSpaceRepository;

    @Test
    void shouldFindByIdParkingAndIdParkingSpaceSuccessTest() {
        Long  idParking = 1L;
        Long idParkingSpace = 2L;
        ParkingSpaceEntity parkingSpaceEntityExpected = new ParkingSpaceEntity(idParkingSpace, 2, true, false, idParking);
        when(parkingSpaceDao.findByIdAndParkingEntityId(idParkingSpace, idParking)).thenReturn(Mono.just(parkingSpaceEntityExpected));
        StepVerifier.create(parkingSpaceRepository.findByIdParkingAndIdParkingSpace(idParking, idParkingSpace))
                .expectNextMatches(parkingSpaceResult -> parkingSpaceResult.getId().equals(idParkingSpace) &&
                        parkingSpaceResult.getParking().getId().equals(idParking) &&
                        parkingSpaceResult.isActive() &&
                        parkingSpaceResult.getOrder().equals(parkingSpaceEntityExpected.getOrderNumber()))
                .verifyComplete();
    }

    @Test
    void shouldSaveSuccessTest() {
        ParkingSpaceEntity parkingSpaceEntityExpected = new ParkingSpaceEntity(1L, 2, true, false, 1L);
        when(parkingSpaceDao.save(argThat(parkingSpaceEntityResult -> parkingSpaceEntityResult.getIdParking().equals(parkingSpaceEntityExpected.getIdParking()) &&
                parkingSpaceEntityResult.getOrderNumber().equals(parkingSpaceEntityExpected.getOrderNumber()) &&
                parkingSpaceEntityResult.isActive() ) )
        ).thenReturn(Mono.just(parkingSpaceEntityExpected));
        StepVerifier.create(parkingSpaceRepository.save(ParkingSpaceMapper.toModel(parkingSpaceEntityExpected)))
                .expectNextMatches(parkingSpaceResult -> parkingSpaceResult.getId().equals(parkingSpaceEntityExpected.getId()) &&
                        parkingSpaceResult.getParking().getId().equals(parkingSpaceEntityExpected.getIdParking()) &&
                        parkingSpaceResult.isActive() &&
                        parkingSpaceResult.getOrder().equals(parkingSpaceEntityExpected.getOrderNumber()))
                .verifyComplete();
    }

    @Test
    void shouldFindByIdSuccessTest() {
        ParkingSpaceEntity parkingSpaceEntityExpected = new ParkingSpaceEntity(1L, 2, true, false, 1L);
        when(parkingSpaceDao.findById(parkingSpaceEntityExpected.getId())).thenReturn(Mono.just(parkingSpaceEntityExpected));
        StepVerifier.create(parkingSpaceRepository.findById(parkingSpaceEntityExpected.getId()))
                .expectNextMatches(parkingSpaceResult -> parkingSpaceResult.getId().equals(parkingSpaceEntityExpected.getId()) &&
                        parkingSpaceResult.getParking().getId().equals(parkingSpaceEntityExpected.getIdParking()) &&
                        parkingSpaceResult.isActive() &&
                        parkingSpaceResult.getOrder().equals(parkingSpaceEntityExpected.getOrderNumber()))
                .verifyComplete();
    }
}