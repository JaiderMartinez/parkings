package co.com.parking.r2dbc.mapper;

import co.com.parking.model.parking.Parking;
import co.com.parking.r2dbc.entities.ParkingEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ParkingMapper {

    public static ParkingEntity toEntity(Parking parking) {
        return ParkingEntity.builder()
                .id(parking.getId())
                .name(parking.getName())
                .hourPrice(parking.getHourPrice())
                .address(parking.getAddress())
                .latitude(parking.getLatitude())
                .longitude(parking.getLongitude())
                .build();
    }

    public static Parking toModel(ParkingEntity parkingEntity) {
        return Parking.builder()
                .id(parkingEntity.getId())
                .name(parkingEntity.getName())
                .hourPrice(parkingEntity.getHourPrice())
                .address(parkingEntity.getAddress())
                .latitude(parkingEntity.getLatitude())
                .longitude(parkingEntity.getLongitude())
                .build();
    }
}
