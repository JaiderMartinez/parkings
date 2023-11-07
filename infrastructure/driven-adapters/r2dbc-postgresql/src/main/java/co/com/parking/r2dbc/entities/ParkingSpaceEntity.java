package co.com.parking.r2dbc.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table(name = "parking_spaces")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParkingSpaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer order;
    private boolean active;
    @Column(name = "location_x")
    private Integer locationX;
    @Column(name = "location_y")
    private Integer locationY;

    @ManyToOne
    @JoinColumn(name = "id_parking", referencedColumnName = "id")
    private ParkingEntity parkingEntity;

    @OneToMany(mappedBy = "parkingSpaceEntity")
    private List<ReserveSpaceEntity> reserveSpacesEntities;
}
