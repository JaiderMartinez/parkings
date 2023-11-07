package co.com.parking.r2dbc.entities;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Table(name = "reserve_space")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReserveSpaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUser;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservationStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservationEndDate;
    private double totalPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parking_space", referencedColumnName = "idParkingSpace")
    private ParkingSpaceEntity parkingSpaceEntity;
}
