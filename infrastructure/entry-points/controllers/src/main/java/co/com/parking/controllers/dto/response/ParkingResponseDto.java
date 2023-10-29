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
public class ParkingResponseDto {

    private Long id;
    private String name;
    private long hourPrice;
    private String address;
}
