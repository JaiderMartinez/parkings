package co.com.parking.usecase;

import co.com.parking.model.parking.Parking;
import co.com.parking.model.parking.ParkingSpace;
import co.com.parking.model.parking.ReserveSpace;
import co.com.parking.model.parking.User;
import co.com.parking.model.parking.config.ErrorCode;
import co.com.parking.model.parking.config.ParkingException;
import co.com.parking.model.parking.gateways.ParkingRepository;
import co.com.parking.model.parking.gateways.UserGateway;
import co.com.parking.model.parking.gateways.ParkingSpaceRepository;
import co.com.parking.model.parking.gateways.ReserveSpaceInParkingRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class ReserveParkingSpaceUseCase {

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ReserveSpaceInParkingRepository reserveSpaceInParkingRepository;
    private final ParkingRepository parkingRepository;
    private final UserGateway userGateway;

    public ReserveParkingSpaceUseCase(ParkingSpaceRepository parkingSpaceRepository,
                                      ReserveSpaceInParkingRepository reserveSpaceInParkingRepository,
                                      ParkingRepository parkingRepository,
                                      UserGateway userGateway) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.reserveSpaceInParkingRepository = reserveSpaceInParkingRepository;
        this.parkingRepository = parkingRepository;
        this.userGateway = userGateway;
    }

    public Mono<ReserveSpace> reserveParkingSpace(Long idParking, Long idUser, Long idParkingSpace) {
        return parkingSpaceRepository.findByIdParkingAndIdParkingSpace(idParking, idParkingSpace)
                .switchIfEmpty(Mono.error(new ParkingException(ErrorCode.F404000)))
                .flatMap(this::validateParkingSpace)
                .flatMap(parkingSpace ->
                    getParking(idParking).flatMap(parking -> {
                        parkingSpace.setParking(parking);
                        parkingSpace.setOccupied(true);
                        return saveParkingSpace(parkingSpace).thenReturn(parkingSpace);
                    })
                )
                .flatMap(parkingSpaceSaved ->
                    getUser(idUser).flatMap(user -> {
                        ReserveSpace reserveSpaceToSave = buildReserveSpace(parkingSpaceSaved, user);
                        return saveReserveSpaceInParking(reserveSpaceToSave)
                                .map(reserveSpace -> {
                                    reserveSpace.setParkingSpace(parkingSpaceSaved);
                                    return reserveSpace;
                                });
                    })
                );
    }

    private Mono<ParkingSpace> validateParkingSpace(ParkingSpace parkingSpaceToTake) {
        if (!parkingSpaceToTake.isActive() || parkingSpaceToTake.isOccupied()) {
            return Mono.error(new ParkingException(ErrorCode.C409000));
        }
        return Mono.just(parkingSpaceToTake);
    }

    private Mono<Parking> getParking(Long idParking) {
        return parkingRepository.findById(idParking)
                .switchIfEmpty(Mono.error(new ParkingException(ErrorCode.F404000)));
    }

    private Mono<ParkingSpace> saveParkingSpace(ParkingSpace parkingSpaceToTake) {
        return parkingSpaceRepository.save(parkingSpaceToTake);
    }

    private Mono<User> getUser(Long idUser) {
        return userGateway.findById(idUser);
    }

    private Mono<ReserveSpace> saveReserveSpaceInParking(ReserveSpace reserveSpace) {
        return reserveSpaceInParkingRepository.save(reserveSpace);
    }

    private ReserveSpace buildReserveSpace(ParkingSpace parkingSpace, User user) {
        return ReserveSpace.builder()
                .reservationStartDate(LocalDateTime.now())
                .parkingSpace(parkingSpace)
                .idUser(user.getId())
                .build();
    }

    public Mono<ReserveSpace> freeUpParkingSpace(Long idParking, Long idParkingSpace, Long idUser) {
        return reserveSpaceInParkingRepository.findOpenReservedSpace(idParking, idParkingSpace, idUser)
                .switchIfEmpty(Mono.error(new ParkingException(ErrorCode.F404000)))
                .flatMap(reserveSpace -> getParkingSpace(idParkingSpace)
                        .map(parkingSpace -> {
                            reserveSpace.setParkingSpace(parkingSpace);
                            return reserveSpace;
                        }))
                .flatMap(reserveSpace -> findParkingAndCalculateTotalToPay(reserveSpace, idParking))
                .flatMap(reserveSpace -> saveReserveSpaceInParking(reserveSpace).thenReturn(reserveSpace))
                .flatMap(reserveSpace -> {
                    reserveSpace.getParkingSpace().setOccupied(false);
                    return saveParkingSpace(reserveSpace.getParkingSpace()).thenReturn(reserveSpace);
                });
    }

    private Mono<ParkingSpace> getParkingSpace(Long idParkingSpace) {
        return parkingSpaceRepository.findById(idParkingSpace)
                .switchIfEmpty(Mono.error(new ParkingException(ErrorCode.F404000)));
    }

    private Mono<ReserveSpace> findParkingAndCalculateTotalToPay(ReserveSpace reserveSpace, Long idParking) {
        return getParking(idParking)
                .map(parking -> {
                    reserveSpace.setReservationEndDate(LocalDateTime.now());
                    reserveSpace.getParkingSpace().setParking(parking);
                    reserveSpace.setTotalPayment(reserveSpace.calculateTotalToPay());
                    return reserveSpace;
                });
    }
}