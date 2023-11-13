package co.com.parking.usecase;

import co.com.parking.model.parking.ReserveSpace;
import co.com.parking.model.parking.gateways.ParkingRepository;
import co.com.parking.usecase.exceptions.FailedTakeParkingSpaceException;
import co.com.parking.model.parking.gateways.ParkingSpaceRepository;
import co.com.parking.model.parking.gateways.ReserveSpaceInParkingRepository;
import co.com.parking.usecase.exceptions.NotFoundException;
import co.com.parking.usecase.exceptions.ParkingNotFoundException;
import co.com.parking.usecase.exceptions.ParkingSpaceNotFoundException;
import co.com.parking.usecase.utils.ErrorMessagesUtil;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

public class ReserveParkingSpaceUseCase {

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ReserveSpaceInParkingRepository reserveSpaceInParkingRepository;
    private final ParkingRepository parkingRepository;

    public ReserveParkingSpaceUseCase(ParkingSpaceRepository parkingSpaceRepository,
                                      ReserveSpaceInParkingRepository reserveSpaceInParkingRepository,
                                      ParkingRepository parkingRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.reserveSpaceInParkingRepository = reserveSpaceInParkingRepository;
        this.parkingRepository = parkingRepository;
    }

    //XXX Reglas para poder tomar el estacionamiento:
    //Estar disponible y no estar ocupado

    //FIXME sino se encuentra elementos que sucede ejemplo un null,
    // deberia añadirlo en el flatmap ademas que pasaria si es null como reacciona el flatmap
    // Sino se llama o se suscribe funciona esto?, en que situaciones debo usarlo? doOnNext
    // Que pasaria si es null doOnNext
    // como funciona esto de los hilos?
    public Mono<ReserveSpace> reserveParkingSpace(Long idParking, Long idUser, Long idParkingSpace) {
        return parkingSpaceRepository.findByIdParkingAndIdParkingSpace(idParking, idParkingSpace)
                .switchIfEmpty(Mono.error(new ParkingNotFoundException()))
                .flatMap( parkingSpaceToTake -> {
                    if (!parkingSpaceToTake.isActive() || parkingSpaceToTake.isBusy()) {
                        return Mono.error(new FailedTakeParkingSpaceException(ErrorMessagesUtil.PARKING_SPACE_NOT_AVAILABLE));
                    }
                    return parkingRepository.findById(idParking)
                            .switchIfEmpty(Mono.error(new ParkingSpaceNotFoundException()))
                            .flatMap( parking -> {
                                parkingSpaceToTake.setBusy(true);
                                parkingSpaceToTake.setParking(parking);
                                return parkingSpaceRepository.save(parkingSpaceToTake)
                                        .thenReturn(parkingSpaceToTake);
                            });
                    }
                ).flatMap( parkingSpaceSaved ->
                         reserveSpaceInParkingRepository.save(ReserveSpace.builder()
                            .reservationStartDate(LocalDateTime.now())
                            .parkingSpace(parkingSpaceSaved)
                            .idUser(idUser)
                            .build())
                                 .flatMap( reserveSpace -> {
                                     reserveSpace.setParkingSpace(parkingSpaceSaved);
                                     return Mono.just(reserveSpace);
                                 })
                );
    }

    // FIXME Existe posibilidad o caso en que un usuario tiene mas de dos reservas de espacios
    public Mono<ReserveSpace> freeUpParkingSpace(Long idParking, Long idUser) {
        return reserveSpaceInParkingRepository.findByIdParkingAndIdUserAndReservationEndDateIsNull(idParking, idUser)
                .switchIfEmpty(Mono.error(new ParkingSpaceNotFoundException()))
                .flatMap(
                    reserveSpace ->
                        parkingRepository.findById(idParking)
                                .switchIfEmpty(Mono.error(new ParkingNotFoundException()))
                                        .flatMap( parking -> {
                                            reserveSpace.setReservationEndDate(LocalDateTime.now());
                                            reserveSpace.getParkingSpace().setParking(parking);
                                            reserveSpace.setTotalPayment(calculateTotalToPay(reserveSpace));
                                            return Mono.just(reserveSpace);
                                        })
                    )
                .flatMap( reserveSpace ->
                    reserveSpaceInParkingRepository.save(reserveSpace)
                            .flatMap( reserveSpaceSaved ->
                                 parkingSpaceRepository.findById(reserveSpace.getParkingSpace().getId())
                                        .switchIfEmpty(Mono.error(new ParkingSpaceNotFoundException()))
                                        .flatMap( parkingSpace -> {
                                            parkingSpace.setBusy(false);
                                            parkingSpace.setParking(reserveSpace.getParkingSpace().getParking());
                                            reserveSpace.setParkingSpace(parkingSpace);
                                            return parkingSpaceRepository.save(parkingSpace)
                                                    .thenReturn(reserveSpace);
                                        })
                            )
                );
    }

    //TODO validar en este caso como obtener el valor double sin tener que usar Mono
    // Es beneficioso que use los publisher?
    private double calculateTotalToPay(ReserveSpace reserveSpace) {
        double parkingPricePerHour = reserveSpace.getParkingSpace().getParking().getHourPrice();
        Duration duration = Duration.between(reserveSpace.getReservationStartDate(), LocalDateTime.now());
        long hoursToPay = duration.toHours();
        return parkingPricePerHour * hoursToPay;
    }

    public Mono<ReserveSpace> reserveParkingSpacePrueba(Long idParking, Long idUser, Long idParkingSpace) {
        return parkingSpaceRepository.findByIdParkingAndIdParkingSpace(idParking, idParkingSpace)
                    .switchIfEmpty(Mono.error(new ParkingNotFoundException()))
                    .flatMap( parkingSpaceToTake -> {
                                if (!parkingSpaceToTake.isActive() || parkingSpaceToTake.isBusy()) {
                                    return Mono.error(new FailedTakeParkingSpaceException(ErrorMessagesUtil.PARKING_SPACE_NOT_AVAILABLE));
                                }
                                return parkingRepository.findById(idParking)
                                        .switchIfEmpty(Mono.error(new ParkingSpaceNotFoundException()))
                                        .flatMap( parking -> {
                                            parkingSpaceToTake.setBusy(true);
                                            parkingSpaceToTake.setParking(parking);
                                            return parkingSpaceRepository.save(parkingSpaceToTake)
                                                    .thenReturn(parkingSpaceToTake);
                                        });
                            }
                    ).flatMap( parkingSpaceSaved ->
                            reserveSpaceInParkingRepository.save(ReserveSpace.builder()
                                            .reservationStartDate(LocalDateTime.now())
                                            .parkingSpace(parkingSpaceSaved)
                                            .idUser(idUser)
                                            .build())
                                    .flatMap( reserveSpace -> {
                                        reserveSpace.setParkingSpace(parkingSpaceSaved);
                                        return Mono.just(reserveSpace);
                                    })
                    );
        }
}
