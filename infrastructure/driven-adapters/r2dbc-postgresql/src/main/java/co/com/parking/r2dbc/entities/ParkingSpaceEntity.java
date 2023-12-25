package co.com.parking.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.transaction.annotation.Transactional;

@Table(name = "parking_spaces")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Transactional
public class ParkingSpaceEntity {

    @Id
    private Long id;
    private Integer orderNumber;
    private boolean active;
    @Column(value = "is_busy")
    private boolean isBusy;
    private Integer locationX;
    private Integer locationY;
    private Long idParking;
}
