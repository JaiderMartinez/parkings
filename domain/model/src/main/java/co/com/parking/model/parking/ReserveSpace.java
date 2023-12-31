package co.com.parking.model.parking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReserveSpace {

    private Long id;
    private Long idUser;
    private LocalDateTime reservationStartDate;
    private LocalDateTime reservationEndDate;
    private double totalPayment;
    private ParkingSpace parkingSpace;

    public double calculateTotalToPay() {
        double parkingPricePerHour = parkingSpace.getParking().getHourPrice();
        Duration duration = Duration.between(reservationStartDate, LocalDateTime.now());
        long hoursToPay = duration.toHours();
        return parkingPricePerHour * hoursToPay;
    }
}
