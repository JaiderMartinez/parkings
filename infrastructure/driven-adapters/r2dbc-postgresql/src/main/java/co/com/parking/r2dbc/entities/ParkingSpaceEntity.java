package co.com.parking.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "parking_spaces")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParkingSpaceEntity {

    @Id
    private Long id;
    @Column(value = "order_number")
    private Integer orderNumber;
    private boolean active;
    @Column(value = "is_busy")
    private boolean isBusy;
    @Column(value = "location_x")
    private Integer locationX;
    @Column(value = "location_y")
    private Integer locationY;
    @Column(value = "id_parking")
    private Long idParking;
}
