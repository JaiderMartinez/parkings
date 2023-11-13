package co.com.parking.controllers.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReserveSpaceResponseDto {

    //TODO porque el orden de los cmapos que muestra es diferente?
    @JsonProperty("id")
    private Long idParking;
    @JsonProperty("nombre")
    private String parkingName;
    private double hourPrice;
    private Integer locationX;
    private Integer locationY;
    private Long idParkingSpace;
}
