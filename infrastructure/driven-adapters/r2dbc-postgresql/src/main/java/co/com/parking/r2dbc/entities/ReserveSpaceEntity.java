package co.com.parking.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
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
    private Long id;
    @Column(value = "id_user")
    private Long idUser;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(value = "reservation_start_date")
    private LocalDateTime reservationStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(value = "reservation_end_date")
    private LocalDateTime reservationEndDate;
    @Column(value = "total_payment")
    private double totalPayment;
    @Column(value = "id_parking_space")
    private Long idParkingSpace;
}
