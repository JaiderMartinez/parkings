package co.com.parking.usecase;

import co.com.parking.model.parking.Parking;
import co.com.parking.model.parking.ParkingSpace;
import co.com.parking.model.parking.ReserveSpace;
import co.com.parking.model.parking.User;
import co.com.parking.model.parking.config.ErrorCode;
import co.com.parking.model.parking.gateways.ParkingRepository;
import co.com.parking.model.parking.gateways.ParkingSpaceRepository;
import co.com.parking.model.parking.gateways.ReserveSpaceInParkingRepository;
import co.com.parking.model.parking.gateways.UserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReserveParkingSpaceUseCaseTest {

    @Mock
    private ParkingSpaceRepository parkingSpaceRepository;
    @Mock
    private ReserveSpaceInParkingRepository reserveSpaceInParkingRepository;
    @Mock
    private ParkingRepository parkingRepository;
    @Mock
    private UserGateway userGateway;
    @InjectMocks
    private ReserveParkingSpaceUseCase reserveParkingSpaceUseCase;

    @Test
    void shouldReserveParkingSpaceSuccessTest() {
        Long idParking = 1L;
        Long idUser = 1L;
        Long idParkingSpace = 1L;
        Parking parking = new Parking(idParking, "name", 1000.0, "address");
        ParkingSpace parkingSpaceExpected = new ParkingSpace(idParkingSpace, 1, true,
                false, -10, 10, parking);
        User user = User.builder()
                .id(idUser)
                .build();
        ReserveSpace reserveSpace = ReserveSpace.builder()
                .reservationStartDate(LocalDateTime.now())
                .parkingSpace(parkingSpaceExpected)
                .idUser(user.getId())
                .build();
        when(parkingSpaceRepository.findByIdParkingAndIdParkingSpace(idParking, idParkingSpace))
                .thenReturn(Mono.just(parkingSpaceExpected));
        when(parkingRepository.findById(idParking)).thenReturn(Mono.just(parking));
        when(parkingSpaceRepository.save(parkingSpaceExpected)).thenReturn(Mono.just(parkingSpaceExpected));
        when(userGateway.findById(idUser)).thenReturn(Mono.just(user));
        when(reserveSpaceInParkingRepository.save( Mockito.argThat( reserveSpaceToSaved ->
                reserveSpaceToSaved.getIdUser().equals(reserveSpace.getIdUser()) ))
        ).thenReturn(Mono.just(reserveSpace));
        StepVerifier.create(reserveParkingSpaceUseCase.reserveParkingSpace(idParking, idUser, idParkingSpace))
                .expectSubscription()
                .expectNext(reserveSpace)
                .verifyComplete();
    }

    @Test
    void reserveParkingSpaceExceptionParkingSpaceNotFoundTest() {
        Long idParking = 1L;
        Long idUser = 1L;
        Long idParkingSpace = 1L;
        when(parkingSpaceRepository.findByIdParkingAndIdParkingSpace(idParking, idParkingSpace)).thenReturn(Mono.empty());
        StepVerifier.create(reserveParkingSpaceUseCase.reserveParkingSpace(idParking, idUser, idParkingSpace))
                .expectErrorMessage(ErrorCode.F404000.getLog())
                .verify();
    }

    @Test
    void reserveParkingSpaceExceptionParkingSpaceBusyTest() {
        Long idParking = 1L;
        Long idUser = 1L;
        Long idParkingSpace = 1L;
        Parking parking = new Parking(idParking, "name", 1000.0, "address");
        ParkingSpace parkingSpaceExpected = new ParkingSpace(idParkingSpace, 1, false,
                false, -10, 10, parking);
        when(parkingSpaceRepository.findByIdParkingAndIdParkingSpace(idParking, idParkingSpace))
                .thenReturn(Mono.just(parkingSpaceExpected));
        StepVerifier.create(reserveParkingSpaceUseCase.reserveParkingSpace(idParking, idUser, idParkingSpace))
                .expectErrorMessage(ErrorCode.C409000.getLog())
                .verify();
    }

    @Test
    void reserveParkingSpaceExceptionParkingNotFoundTest() {
        Long idParking = 1L;
        Long idUser = 1L;
        Long idParkingSpace = 1L;
        Parking parking = new Parking(idParking, "name", 1000.0, "address");
        ParkingSpace parkingSpaceExpected = new ParkingSpace(idParkingSpace, 1, true,
                false, -10, 10, parking);
        when(parkingSpaceRepository.findByIdParkingAndIdParkingSpace(idParking, idParkingSpace))
                .thenReturn(Mono.just(parkingSpaceExpected));
        when(parkingRepository.findById(idParking)).thenReturn(Mono.empty());
        StepVerifier.create(reserveParkingSpaceUseCase.reserveParkingSpace(idParking, idUser, idParkingSpace))
                .expectErrorMessage(ErrorCode.F404000.getLog())
                .verify();
    }

    @Test
    void shouldFreeUpParkingSpaceSuccessTest() {
        Long idParking = 1L;
        Long idUser = 1L;
        Long idParkingSpace = 1L;
        Parking parking = new Parking(idParking, "name", 1000.0, "address");
        ParkingSpace parkingSpaceExpected = new ParkingSpace(idParkingSpace, 1, true,
                false, -10, 10, parking);
        ReserveSpace reserveSpaceExpected = ReserveSpace.builder()
                .id(1L)
                .reservationStartDate(LocalDateTime.now())
                .reservationEndDate(LocalDateTime.now().plusHours(2))
                .parkingSpace(parkingSpaceExpected)
                .idUser(idUser)
                .build();
        when(reserveSpaceInParkingRepository.findOpenReservedSpace(idParking, idParkingSpace, idUser)).thenReturn(Mono.just(reserveSpaceExpected));
        when(parkingRepository.findById(idParking)).thenReturn(Mono.just(parking));
        when(reserveSpaceInParkingRepository.save(argThat(reserveSpace ->
                reserveSpace.getId().equals(reserveSpaceExpected.getId()) ) )
        ).thenReturn(Mono.just(reserveSpaceExpected));
        when(parkingSpaceRepository.findById(idParkingSpace)).thenReturn(Mono.just(parkingSpaceExpected));
        when(parkingSpaceRepository.save(argThat(parkingSpace ->
                parkingSpace.getId().equals(parkingSpaceExpected.getId()) ) )
        ).thenReturn(Mono.just(parkingSpaceExpected));
        StepVerifier.create(reserveParkingSpaceUseCase.freeUpParkingSpace(idParking, idParkingSpace, idUser))
                .expectSubscription()
                .expectNext(reserveSpaceExpected)
                .verifyComplete();
    }

    @Test
    void feeUpParkingSpaceExceptionReserveSpaceInParkingNotFoundTest() {
        Long idParking = 1L;
        Long idUser = 1L;
        Long idParkingSpace = 1L;
        when(reserveSpaceInParkingRepository.findOpenReservedSpace(idParking, idParkingSpace, idUser)).thenReturn(Mono.empty());
        StepVerifier.create(reserveParkingSpaceUseCase.freeUpParkingSpace(idParking, idParkingSpace, idUser))
                .expectSubscription()
                .expectErrorMessage(ErrorCode.F404000.getLog())
                .verify();
    }

    @Test
    void freeUpParkingSpaceExceptionParkingSpaceNotFoundTest() {
        Long idParking = 1L;
        Long idUser = 1L;
        Long idParkingSpace = 1L;
        ParkingSpace parkingSpaceExpected = ParkingSpace.builder()
                .id(idParkingSpace)
                .build();
        ReserveSpace reserveSpaceExpected = ReserveSpace.builder()
                .id(1L)
                .reservationStartDate(LocalDateTime.now())
                .parkingSpace(parkingSpaceExpected)
                .idUser(idUser)
                .build();
        when(reserveSpaceInParkingRepository.findOpenReservedSpace(idParking, idParkingSpace, idUser)).thenReturn(Mono.just(reserveSpaceExpected));
        when(parkingSpaceRepository.findById(idParkingSpace)).thenReturn(Mono.empty());
        StepVerifier.create(reserveParkingSpaceUseCase.freeUpParkingSpace(idParking, idParkingSpace, idUser))
                .expectSubscription()
                .expectErrorMessage(ErrorCode.F404000.getLog())
                .verify();
    }

    @Test
    void freeUpParkingSpaceExceptionParkingNotFoundTest() {
        Long idParking = 1L;
        Long idUser = 1L;
        Long idParkingSpace = 1L;
        ParkingSpace parkingSpaceExpected = ParkingSpace.builder()
                .id(idParkingSpace)
                .order(1)
                .isActive(true)
                .isBusy(true)
                .locationX(-10)
                .locationY(10)
                .build();
        ReserveSpace reserveSpaceExpected = ReserveSpace.builder()
                .id(1L)
                .reservationStartDate(LocalDateTime.now())
                .reservationEndDate(LocalDateTime.now().plusHours(2))
                .parkingSpace(parkingSpaceExpected)
                .idUser(idUser)
                .build();
        when(reserveSpaceInParkingRepository.findOpenReservedSpace(idParking, idParkingSpace, idUser)).thenReturn(Mono.just(reserveSpaceExpected));
        when(parkingSpaceRepository.findById(idParkingSpace)).thenReturn(Mono.just(parkingSpaceExpected));
        when(parkingRepository.findById(idParking)).thenReturn(Mono.empty());
        StepVerifier.create(reserveParkingSpaceUseCase.freeUpParkingSpace(idParking, idParkingSpace, idUser))
                .expectSubscription()
                .expectErrorMessage(ErrorCode.F404000.getLog())
                .verify();
    }
}