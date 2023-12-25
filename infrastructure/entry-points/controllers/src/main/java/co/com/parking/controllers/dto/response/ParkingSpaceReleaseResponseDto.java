package co.com.parking.controllers.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("id")
    private Long idParking;
    @JsonProperty("nombre")
    private String parkingName;
    private double hourPrice;
    private double totalPayment;
}
