package co.com.parking.r2dbc.repository;

import co.com.parking.r2dbc.dao.ReserveSpaceDao;
import co.com.parking.r2dbc.entities.ReserveSpaceEntity;
import co.com.parking.r2dbc.mapper.ReserveSpaceMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReserveSpaceInParkingRepositoryImplTest {

    @Mock
    private ReserveSpaceDao reserveSpaceDao;
    @InjectMocks
    private ReserveSpaceInParkingRepositoryImpl reserveSpaceInParkingRepository;

    @Test
    void shouldSaveSuccessTest() {
        ReserveSpaceEntity reserveSpaceEntityExpected = ReserveSpaceEntity.builder()
                .idUser(1L)
                .reservationStartDate(LocalDateTime.now())
                .idParkingSpace(1L)
                .build();
        when(reserveSpaceDao.save(argThat(reserveSpaceRequest -> reserveSpaceRequest.getIdParkingSpace().equals(reserveSpaceEntityExpected.getIdParkingSpace()) &&
                reserveSpaceRequest.getIdUser().equals(reserveSpaceEntityExpected.getIdUser()) &&
                reserveSpaceRequest.getReservationStartDate().equals(reserveSpaceEntityExpected.getReservationStartDate()) ) )
        ).thenReturn(Mono.just(reserveSpaceEntityExpected));
        StepVerifier.create(reserveSpaceInParkingRepository.save(ReserveSpaceMapper.toModel(reserveSpaceEntityExpected)))
                .expectNextMatches(reserveSpaceResult -> reserveSpaceResult.getParkingSpace().getId().equals(reserveSpaceEntityExpected.getIdParkingSpace()) &&
                        reserveSpaceResult.getIdUser().equals(reserveSpaceEntityExpected.getIdUser()) &&
                        reserveSpaceResult.getReservationStartDate().equals(reserveSpaceEntityExpected.getReservationStartDate()))
                .verifyComplete();
    }

    @Test
    void shouldFindOpenReservedSpaceSuccessTest() {
        Long idParking = 1L;
        Long idParkingSpace = 1L;
        Long idUser = 1L;
        ReserveSpaceEntity reserveSpaceEntityExpected = ReserveSpaceEntity.builder()
                .id(1L)
                .idUser(idUser)
                .reservationStartDate(LocalDateTime.now())
                .idParkingSpace(idParkingSpace)
                .build();
        when(reserveSpaceDao.findOpenReservedSpace(idParking, idParkingSpace, idUser)).thenReturn(Mono.just(reserveSpaceEntityExpected));
        StepVerifier.create(reserveSpaceInParkingRepository.findOpenReservedSpace(idParking, idParkingSpace, idUser))
                .expectNextMatches(reserveSpaceResult -> reserveSpaceResult.getParkingSpace().getId().equals(reserveSpaceEntityExpected.getIdParkingSpace()) &&
                        reserveSpaceResult.getIdUser().equals(reserveSpaceEntityExpected.getIdUser()) &&
                        reserveSpaceResult.getReservationStartDate().equals(reserveSpaceEntityExpected.getReservationStartDate()))
                .verifyComplete();
    }
}