package co.com.parking.model.parking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReserveSpace {

    private Long id;
    private Long idUser;
    private LocalDateTime reservationStartDate;//tomo el espacio
    private LocalDateTime reservationEndDate;//Desocupo el espacio de estacionamiento
    private double totalPayment;
    private ParkingSpace parkingSpace;
}
