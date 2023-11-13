package co.com.parking.controllers.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParkingLotOccupyRequestDto {

    @NotNull
    private Long idUser;
    private Long parkingId;
    private Long idParkingSpace;
    private String hola;
}
