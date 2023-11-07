package co.com.parking.usecase;

import co.com.parking.model.parking.ParkingSpace;
import co.com.parking.model.parking.ReserveSpace;
import co.com.parking.usecase.exceptions.FailedTakeParkingSpaceException;
import co.com.parking.model.parking.gateways.ParkingSpaceRepository;
import co.com.parking.model.parking.gateways.ReserveSpaceInParkingRepository;
import co.com.parking.usecase.exceptions.NotFoundException;
import co.com.parking.usecase.exceptions.ParkingSpaceNotFoundException;
import co.com.parking.usecase.utils.ErrorMessagesUtil;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

public class ReserveParkingSpaceUseCase {

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ReserveSpaceInParkingRepository reserveSpaceInParkingRepository;

    public ReserveParkingSpaceUseCase(ParkingSpaceRepository parkingSpaceRepository,
                                      ReserveSpaceInParkingRepository reserveSpaceInParkingRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.reserveSpaceInParkingRepository = reserveSpaceInParkingRepository;
    }

    //Reglas para poder tomar el estacionamiento:
    //Estar disponible y no estar ocupado

    // sino se encuentra elementos que sucede ejemplo un null,
    // deberia añadirlo en el flatmap ademas que pasaria si es null como reacciona el flatmap
    //Sino se llama o se suscribe funciona esto?, en que situaciones debo usarlo? doOnNext
    //Que pasaria si es null doOnNext
    // como funciona esto de los hilos?
    public Mono<ReserveSpace> reserveParkingSpace(Long idParking, Long idUser, Long idParkingSpace) {
        return parkingSpaceRepository.findByIdParkingAndIdParkingSpace(idParking, idParkingSpace)
                .switchIfEmpty(Mono.error(new ParkingSpaceNotFoundException()))
                .flatMap( parkingSpaceToTake -> {
                    if (!parkingSpaceToTake.isActive() || parkingSpaceToTake.isBusy()) {
                        return Mono.error(new FailedTakeParkingSpaceException(ErrorMessagesUtil.PARKING_SPACE_NOT_AVAILABLE));
                    }
                    parkingSpaceToTake.setBusy(true);
                    return parkingSpaceRepository.save(parkingSpaceToTake);//el object parking es null
                    }
                ).flatMap( parkingSpaceSaved ->
                         reserveSpaceInParkingRepository.save(ReserveSpace.builder()
                            .reservationStartDate(LocalDateTime.now())
                            .parkingSpace(parkingSpaceSaved)
                            .idUser(idUser)
                            .build())
                );
    }

    //Existe posibilidad o caso en que un usuario tiene mas de dos reservas de espacios
    public Mono<ReserveSpace> freeUpParkingSpace(Long idParking, Long idUser) {
        return reserveSpaceInParkingRepository.findByIdParkingAndIdUserAndReservationEndDateIsNull(idParking, idUser)
                .switchIfEmpty(Mono.error(new NotFoundException(ErrorMessagesUtil.RESERVATION_NOT_FOUND)))
                .flatMap(
                    reserveSpace -> {
                        reserveSpace.setReservationEndDate(LocalDateTime.now());
                        reserveSpace.setTotalPayment(calculateTotalToPay(reserveSpace));
                        return reserveSpaceInParkingRepository.save(reserveSpace);
                    });
    }

    private double calculateTotalToPay(ReserveSpace reserveSpace) {
        ParkingSpace parkingSpace = reserveSpace.getParkingSpace();
        double parkingPricePerHour = parkingSpace.getParking().getHourPrice();
        Duration duration = Duration.between(reserveSpace.getReservationStartDate(), LocalDateTime.now());
        long hoursToPay = duration.toHours();
        return parkingPricePerHour * hoursToPay;
    }
}
