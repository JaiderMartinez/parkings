package co.com.parking.r2dbc.mapper;

import co.com.parking.model.parking.ParkingSpace;
import co.com.parking.model.parking.ReserveSpace;
import co.com.parking.r2dbc.entities.ReserveSpaceEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReserveSpaceMapper {

    public static ReserveSpace toModel(ReserveSpaceEntity reserveSpaceEntity) {
        return ReserveSpace.builder()
                .id(reserveSpaceEntity.getId())
                .idUser(reserveSpaceEntity.getIdUser())
                .reservationStartDate(reserveSpaceEntity.getReservationStartDate())
                .reservationEndDate(reserveSpaceEntity.getReservationEndDate())
                .totalPayment(reserveSpaceEntity.getTotalPayment())
                .parkingSpace(ParkingSpace.builder()
                        .id(reserveSpaceEntity.getIdParkingSpace())
                        .build())
                .build();
    }

    public static ReserveSpaceEntity toEntity(ReserveSpace reserveSpace) {
        return ReserveSpaceEntity.builder()
                .id(reserveSpace.getId())
                .idUser(reserveSpace.getIdUser())
                .reservationStartDate(reserveSpace.getReservationStartDate())
                .reservationEndDate(reserveSpace.getReservationEndDate())
                .totalPayment(reserveSpace.getTotalPayment())
                .idParkingSpace(reserveSpace.getParkingSpace().getId())
                .build();
    }
}
