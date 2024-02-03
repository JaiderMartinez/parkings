package co.com.parking.r2dbc.repository;

import co.com.parking.r2dbc.dao.ParkingDao;
import co.com.parking.r2dbc.entities.ParkingEntity;
import co.com.parking.r2dbc.mapper.ParkingMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingRepositoryImplTest {

    private static final String VALUE_PROPERTIES_LIMIT_PARKING_LOTS_BY_LOCATION = "2";
    @Mock
    private ParkingDao parkingDao;
    @InjectMocks
    private ParkingRepositoryImpl parkingRepository;

    @Test
    void shouldSaveSuccessTest() {
        ParkingEntity parkingEntityExpected = new ParkingEntity(1L, "Parking 1", 1000, "Calle 2", -10, 10);
        when(parkingDao.save(argThat( (parkingEntityRequest) -> parkingEntityRequest.getName().equals(parkingEntityExpected.getName())) )
        ).thenReturn(Mono.just(parkingEntityExpected));
        StepVerifier.create(parkingRepository.save(ParkingMapper.toModel(parkingEntityExpected)))
                .expectNextMatches(parkingResult -> parkingResult.getName().equals(parkingEntityExpected.getName()))
                .verifyComplete();
    }

    @Test
    void shouldFindAllSuccessTest() {
        ParkingEntity parkingEntityExpected = new ParkingEntity(1L, "Parking 1", 1000, "Calle 2", -10, 10);
        when(parkingDao.findAll()).thenReturn(Flux.just(parkingEntityExpected));
        StepVerifier.create(parkingRepository.findAll())
                .expectNextMatches(parkingResult -> parkingResult.getName().equals(parkingEntityExpected.getName()))
                .verifyComplete();
    }

    @Test
    void shouldFindByIdSuccessTest() {
        Long idParking = 1L;
        ParkingEntity parkingEntityExpected = new ParkingEntity(1L, "Parking 1", 1000, "Calle 2", -10, 10);
        when(parkingDao.findById(idParking)).thenReturn(Mono.just(parkingEntityExpected));
        StepVerifier.create(parkingRepository.findById(idParking))
                .expectNextMatches(parkingResult -> parkingResult.getName().equals(parkingEntityExpected.getName()))
                .verifyComplete();
    }

    @Test
    void shouldFindByLocationSuccessTest() {
        double latitudeInRadians = -49;
        double longitudeInRadians = 4;
        ParkingEntity parkingEntityExpected = new ParkingEntity(1L, "Parking 1", 1000, "Calle 2",
                Math.toDegrees(latitudeInRadians), Math.toDegrees(longitudeInRadians));
        when(parkingDao.findByLocation(latitudeInRadians, longitudeInRadians, Integer.parseInt(VALUE_PROPERTIES_LIMIT_PARKING_LOTS_BY_LOCATION))).thenReturn(Flux.just(parkingEntityExpected));
        StepVerifier.create(parkingRepository.findByLocation(latitudeInRadians, longitudeInRadians))
                .expectNext(ParkingMapper.toModel(parkingEntityExpected))
                .verifyComplete();
    }
}