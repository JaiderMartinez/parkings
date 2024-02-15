package co.com.parking.r2dbc.repository;

import co.com.parking.model.parking.Parking;
import co.com.parking.r2dbc.dao.ParkingDao;
import co.com.parking.r2dbc.entities.ParkingEntity;
import co.com.parking.r2dbc.mapper.ParkingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingRepositoryImplTest {

    private static final String VALUE_PROPERTIES_LIMIT_PARKING_LOTS_BY_LOCATION = "2";
    private ParkingDao parkingDao;
    private ParkingRepositoryImpl parkingRepository;
    private ParkingEntity parkingEntityExpected;

    @BeforeEach
    void setUp() {
        parkingDao = mock(ParkingDao.class);
        parkingRepository = new ParkingRepositoryImpl(VALUE_PROPERTIES_LIMIT_PARKING_LOTS_BY_LOCATION, parkingDao);
        parkingEntityExpected = ParkingEntity.builder()
                .id(1L)
                .address("address")
                .name("name")
                .hourPrice(1000)
                .latitude(10)
                .longitude(-10)
                .build();
    }

    @Test
    void shouldSaveSuccessTest() {
        when(parkingDao.save(argThat( (parkingEntityRequest) ->
                parkingEntityRequest.getName().equals(parkingEntityExpected.getName())) ))
                .thenReturn(Mono.just(parkingEntityExpected));
        StepVerifier.create(parkingRepository.save(ParkingMapper.toModel(parkingEntityExpected)))
                .expectNextMatches(parkingResult -> parkingResult.getName().equals(parkingEntityExpected.getName()))
                .verifyComplete();
    }

    @Test
    void shouldFindAllSuccessTest() {
        when(parkingDao.findAll()).thenReturn(Flux.just(parkingEntityExpected));
        assertMatches(parkingRepository.findAll());
    }

    @Test
    void shouldFindByIdSuccessTest() {
        Long idParking = 1L;
        when(parkingDao.findById(idParking)).thenReturn(Mono.just(parkingEntityExpected));
        StepVerifier.create(parkingRepository.findById(idParking))
                .expectNextMatches(parkingResult -> parkingResult.getName().equals(parkingEntityExpected.getName()))
                .verifyComplete();
    }

    @Test
    void shouldFindByLocationSuccessTest() {
        double latitudeInRadians = -49;
        double longitudeInRadians = 4;
        parkingEntityExpected.setLongitude(Math.toDegrees(longitudeInRadians));
        parkingEntityExpected.setLatitude(Math.toDegrees(latitudeInRadians));
        when(parkingDao.findByLocation(latitudeInRadians, longitudeInRadians, Integer.parseInt(VALUE_PROPERTIES_LIMIT_PARKING_LOTS_BY_LOCATION)))
                .thenReturn(Flux.just(parkingEntityExpected));
        assertMatches(parkingRepository.findByLocation(latitudeInRadians, longitudeInRadians));
    }

    private void assertMatches(Flux<Parking> parkings) {
        StepVerifier.create(parkings)
                .expectNextMatches(
                        parking ->
                                parking.getId().equals(parkingEntityExpected.getId()) &&
                                parking.getName().equals(parkingEntityExpected.getName()) &&
                                parking.getAddress().equals(parkingEntityExpected.getAddress()) &&
                                parking.getHourPrice() == parkingEntityExpected.getHourPrice() &&
                                parking.getLatitude() == parkingEntityExpected.getLatitude() &&
                                parking.getLongitude() == parkingEntityExpected.getLongitude()
                )
                .verifyComplete();
    }
}