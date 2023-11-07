package co.com.parking.r2dbc.mapper;

import co.com.parking.model.parking.ParkingSpace;
import co.com.parking.r2dbc.entities.ParkingSpaceEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ParkingSpaceMapper {

    public static ParkingSpace toModel(ParkingSpaceEntity parkingSpaceEntity) {
        return ParkingSpace.builder()
                .id(parkingSpaceEntity.getId())
                .order(parkingSpaceEntity.getOrder())
                .active(parkingSpaceEntity.isActive())
                .locationX(parkingSpaceEntity.getLocationX())
                .locationY(parkingSpaceEntity.getLocationY())
                .parking(parkingSpaceEntity.getParkingEntity() == null ? null :
                        ParkingMapper.toModel(parkingSpaceEntity.getParkingEntity()))
                .build();
    }

    public static ParkingSpaceEntity toEntity(ParkingSpace parkingSpace) {
        return ParkingSpaceEntity.builder()
                .id(parkingSpace.getId())
                .order(parkingSpace.getOrder())
                .active(parkingSpace.isActive())
                .locationX(parkingSpace.getLocationX())
                .locationY(parkingSpace.getLocationY())
                .parkingEntity(ParkingMapper.toEntity(parkingSpace.getParking()))
                .build();
    }
}
