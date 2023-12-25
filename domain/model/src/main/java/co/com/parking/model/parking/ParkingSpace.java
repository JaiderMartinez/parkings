package co.com.parking.model.parking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ParkingSpace {

    private Long id;
    private Integer order;
    private boolean isActive;
    private boolean isBusy;
    private Integer locationX;
    private Integer locationY;
    private Parking parking;
}
