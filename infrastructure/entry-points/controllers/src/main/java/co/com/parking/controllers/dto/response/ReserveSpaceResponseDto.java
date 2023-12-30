package co.com.parking.controllers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReserveSpaceResponseDto {

    private Long idParking;
    private String parkingName;
    private double hourPrice;
    private Long idParkingSpace;
}
