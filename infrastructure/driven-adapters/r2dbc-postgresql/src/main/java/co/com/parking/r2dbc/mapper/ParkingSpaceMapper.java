package co.com.parking.r2dbc.mapper;

import co.com.parking.model.parking.Parking;
import co.com.parking.model.parking.ParkingSpace;
import co.com.parking.r2dbc.entities.ParkingSpaceEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ParkingSpaceMapper {

    public static ParkingSpace toModel(ParkingSpaceEntity parkingSpaceEntity) {
        return ParkingSpace.builder()
                .id(parkingSpaceEntity.getId())
                .order(parkingSpaceEntity.getOrderNumber())
                .isActive(parkingSpaceEntity.isActive())
                .isOccupied(parkingSpaceEntity.isOccupied())
                .parking(Parking.builder()
                        .id(parkingSpaceEntity.getIdParking())
                        .build())
                .build();
    }

    public static ParkingSpaceEntity toEntity(ParkingSpace parkingSpace) {
        return ParkingSpaceEntity.builder()
                .id(parkingSpace.getId())
                .orderNumber(parkingSpace.getOrder())
                .active(parkingSpace.isActive())
                .isOccupied(parkingSpace.isOccupied())
                .idParking(parkingSpace.getParking().getId())
                .build();
    }
}
