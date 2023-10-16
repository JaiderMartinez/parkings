package co.com.parking.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "parkings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingEntity {

    @Id
    private Long id;
}
