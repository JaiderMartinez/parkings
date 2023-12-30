package co.com.parking.controllers.dto.response;

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
public class ParkingSpaceReleaseResponseDto {

    private Long idParking;
    private String parkingName;
    private double hourPrice;
    private double totalPayment;
}
