package co.com.parking.model.parking;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Parking {

    private Long id;
    private String name;
    private double hourPrice;
    private String address;
    private double latitude;
    private double longitude;
}
