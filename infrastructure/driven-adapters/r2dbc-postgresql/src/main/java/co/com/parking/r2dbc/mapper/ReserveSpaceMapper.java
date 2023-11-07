package co.com.parking.r2dbc.mapper;

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
                .parkingSpace(ParkingSpaceMapper.toModel(reserveSpaceEntity.getParkingSpaceEntity()))
                .build();
    }

    public static ReserveSpaceEntity toEntity(ReserveSpace reserveSpace) {
        return ReserveSpaceEntity.builder()
                .id(reserveSpace.getId())
                .idUser(reserveSpace.getIdUser())
                .reservationStartDate(reserveSpace.getReservationStartDate())
                .reservationEndDate(reserveSpace.getReservationEndDate())
                .totalPayment(reserveSpace.getTotalPayment())
                .parkingSpaceEntity(ParkingSpaceMapper.toEntity(reserveSpace.getParkingSpace()))
                .build();
    }
}
