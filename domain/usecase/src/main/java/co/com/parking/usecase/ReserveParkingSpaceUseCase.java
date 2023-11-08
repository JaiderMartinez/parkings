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
                                return parkingSpaceRepository.save(parkingSpaceToTake);
                            });
                    }
                ).flatMap( parkingSpaceSaved ->
                         reserveSpaceInParkingRepository.save(ReserveSpace.builder()
                            .reservationStartDate(LocalDateTime.now())
                            .parkingSpace(parkingSpaceSaved)
                            .idUser(idUser)
                            .build())
                );
    }

    // FIXME Existe posibilidad o caso en que un usuario tiene mas de dos reservas de espacios
    public Mono<ReserveSpace> freeUpParkingSpace(Long idParking, Long idUser) {
        return reserveSpaceInParkingRepository.findByIdParkingAndIdUserAndReservationEndDateIsNull(idParking, idUser)
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessagesUtil.RESERVATION_NOT_FOUND)))
                .flatMap(
                    reserveSpace ->
                        calculateTotalToPay(reserveSpace)
                                .flatMap( totalPayment -> {
                                    reserveSpace.setReservationEndDate(LocalDateTime.now());
                                    reserveSpace.setTotalPayment(totalPayment);
                                    return Mono.just(reserveSpace);
                                })
                    )
                .flatMap(reserveSpaceInParkingRepository::save);
    }

    //TODO validar en este caso como obtener el valor double sin tener que usar Mono
    // Es beneficioso que use los publisher?
    private Mono<Double> calculateTotalToPay(ReserveSpace reserveSpace) {
        return parkingRepository.findById(reserveSpace.getParkingSpace().getParking().getId())
                .flatMap( parking -> {
                    double parkingPricePerHour = parking.getHourPrice();
                    Duration duration = Duration.between(reserveSpace.getReservationStartDate(), LocalDateTime.now());
                    long hoursToPay = duration.toHours();
                    return Mono.just(parkingPricePerHour * hoursToPay);
                });
    }
}
