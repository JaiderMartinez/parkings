package co.com.parking.controllers.mapper;

import co.com.parking.controllers.dto.request.ParkingRequestDto;
import co.com.parking.controllers.dto.response.ParkingResponseDto;
import co.com.parking.model.parking.Parking;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ParkingDtoMapper {

    public static ParkingResponseDto toResponseDto(Parking parking) {
        return ParkingResponseDto.builder()
                .id(parking.getId())
                .name(parking.getName())
                .hourPrice(parking.getHourPrice())
                .address(parking.getAddress())
                .latitude(parking.getLatitude())
                .longitude(parking.getLongitude())
                .build();
    }

    public static Parking toModel(ParkingRequestDto parkingRequestDto) {
        return Parking.builder()
                .address(parkingRequestDto.getAddress())
                .name(parkingRequestDto.getName())
                .hourPrice(parkingRequestDto.getHourPrice())
                .latitude(parkingRequestDto.getLatitude())
                .longitude(parkingRequestDto.getLongitude())
                .build();
    }
}
