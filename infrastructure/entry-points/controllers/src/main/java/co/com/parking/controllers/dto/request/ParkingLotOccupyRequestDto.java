package co.com.parking.controllers.dto.request;

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

    private Long idUser;
    private Long parkingId;
    private Long idParkingSpace;
}
