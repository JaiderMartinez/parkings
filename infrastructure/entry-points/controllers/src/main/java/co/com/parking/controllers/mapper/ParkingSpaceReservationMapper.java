package co.com.parking.controllers.mapper;

import co.com.parking.controllers.dto.response.ParkingSpaceReleaseResponseDto;
import co.com.parking.controllers.dto.response.ReserveSpaceResponseDto;
import co.com.parking.model.parking.ReserveSpace;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ParkingSpaceReservationMapper {

    public static ReserveSpaceResponseDto toResponseDto(ReserveSpace reserveSpace) {
        return ReserveSpaceResponseDto.builder()
                .idParkingSpace(reserveSpace.getId())
                .idParking(reserveSpace.getParkingSpace().getId())
                //.parkingName(reserveSpace.getParkingSpace().getParking().getName())
                //.hourPrice(reserveSpace.getParkingSpace().getParking().getHourPrice())
                //.locationX(reserveSpace.getParkingSpace().getLocationX())
                //.locationY(reserveSpace.getParkingSpace().getLocationY())
                .build();
    }

    public static ParkingSpaceReleaseResponseDto toParkingSpaceReleaseResponseDto(ReserveSpace reserveSpace) {
        return ParkingSpaceReleaseResponseDto.builder()
                .idParking(reserveSpace.getParkingSpace().getId())
                .parkingName(reserveSpace.getParkingSpace().getParking().getName())
                .hourPrice(reserveSpace.getParkingSpace().getParking().getHourPrice())
                .totalPayment(reserveSpace.getTotalPayment())
                .build();
    }
}
