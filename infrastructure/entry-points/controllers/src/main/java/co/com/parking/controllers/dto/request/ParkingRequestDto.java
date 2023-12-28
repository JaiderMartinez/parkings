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
public class ParkingRequestDto {

    private String name;
    private long hourPrice;
    private String address;
    private double latitude;
    private double longitude;
}
