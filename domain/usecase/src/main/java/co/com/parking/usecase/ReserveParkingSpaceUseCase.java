package co.com.parking.usecase;

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
                //TODO validar que no tenga ya un espacio reservado
                .flatMap(parkingSpace -> validateAndSavedParkingSpaceBusy(parkingSpace, idParking))
                .flatMap(parkingSpaceSaved -> existsUserAndSaveReservedSpace(parkingSpaceSaved, idUser));
    }

    private Mono<ParkingSpace> validateAndSavedParkingSpaceBusy(ParkingSpace parkingSpaceToTake, Long idParking) {
        if (!parkingSpaceToTake.isActive() || parkingSpaceToTake.isBusy()) {
            return Mono.error(new ParkingException(ErrorCode.C409000));
        }
        return parkingRepository.findById(idParking)
                .switchIfEmpty(Mono.error(new ParkingException(ErrorCode.S204000)))
                .flatMap(parking -> {
                    parkingSpaceToTake.setBusy(true);
                    parkingSpaceToTake.setParking(parking);
                    return parkingSpaceRepository.save(parkingSpaceToTake)
                            .thenReturn(parkingSpaceToTake);
                });
    }

    private Mono<ReserveSpace> existsUserAndSaveReservedSpace(ParkingSpace parkingSpaceSaved, Long idUser) {
        return userGateway.findById(idUser)
                .flatMap(user ->
                        reserveSpaceInParkingRepository.save(buildReserveSpace(parkingSpaceSaved, user))
                                .map(reserveSpace -> {
                                    reserveSpace.setParkingSpace(parkingSpaceSaved);
                                    return reserveSpace;
                                })
                );
    }

    private ReserveSpace buildReserveSpace(ParkingSpace parkingSpace, User user) {
        return ReserveSpace.builder()
                .reservationStartDate(LocalDateTime.now())
                .parkingSpace(parkingSpace)
                .idUser(user.getId())
                .build();
    }

    public Mono<ReserveSpace> freeUpParkingSpace(Long idParking, Long idUser) {
        return reserveSpaceInParkingRepository.findByIdParkingAndIdUserAndReservationEndDateIsNull(idParking, idUser)
                .switchIfEmpty(Mono.error(new ParkingException(ErrorCode.B400000)))
                .flatMap(reserveSpace -> findParkingAndCalculateTotalToPay(reserveSpace, idParking))
                .flatMap(this::updateReserveSpaceAndEnableParkingSpace);
    }

    private Mono<ReserveSpace> findParkingAndCalculateTotalToPay(ReserveSpace reserveSpace, Long idParking) {
        return parkingRepository.findById(idParking)
                .switchIfEmpty(Mono.error(new ParkingException(ErrorCode.B400000)))
                .map(parking -> {
                    reserveSpace.setReservationEndDate(LocalDateTime.now());
                    reserveSpace.getParkingSpace().setParking(parking);
                    reserveSpace.setTotalPayment(reserveSpace.calculateTotalToPay());
                    return reserveSpace;
                });
    }

    private Mono<ReserveSpace> updateReserveSpaceAndEnableParkingSpace(ReserveSpace reserveSpace) {
        return reserveSpaceInParkingRepository.save(reserveSpace)
                .flatMap(reserveSpaceSaved ->
                        parkingSpaceRepository.findById(reserveSpace.getParkingSpace().getId())
                                .switchIfEmpty(Mono.error(new ParkingException(ErrorCode.B400000)))
                                .flatMap(parkingSpace -> {
                                    parkingSpace.setBusy(false);
                                    parkingSpace.setParking(reserveSpace.getParkingSpace().getParking());
                                    reserveSpace.setParkingSpace(parkingSpace);
                                    return parkingSpaceRepository.save(parkingSpace)
                                            .thenReturn(reserveSpace);
                                })
                );
    }
}