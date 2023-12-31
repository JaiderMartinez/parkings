package co.com.parking.controllers;

import co.com.parking.controllers.dto.request.ParkingLotOccupyRequestDto;
import co.com.parking.controllers.dto.response.ParkingSpaceReleaseResponseDto;
import co.com.parking.controllers.dto.response.ReserveSpaceResponseDto;
import co.com.parking.controllers.mapper.ParkingSpaceReservationDtoMapper;
import co.com.parking.usecase.ReserveParkingSpaceUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/parking-lot")
@RequiredArgsConstructor
@Validated
public class ReserveParkingSpaceController {

    private final ReserveParkingSpaceUseCase reserveParkingSpaceUseCase;

    @PostMapping("/occupy")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ReserveSpaceResponseDto> reserveParkingSpace(@Valid @RequestBody ParkingLotOccupyRequestDto parkingLotOccupyRequestDto) {
        return reserveParkingSpaceUseCase.reserveParkingSpace(parkingLotOccupyRequestDto.getParkingId(),
                                                               parkingLotOccupyRequestDto.getIdUser(),
                                                               parkingLotOccupyRequestDto.getIdParkingSpace())
                .map(ParkingSpaceReservationDtoMapper::toResponseDto);
    }

    @PatchMapping("/leave/{idParking}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ParkingSpaceReleaseResponseDto> freeUpParkingSpace(
            @PathVariable(name = "idParking") Long idParking,
            @RequestParam(name = "idParkingSpace") Long idParkingSpace,
            @RequestParam(name = "idUser") Long idUser) {
        return reserveParkingSpaceUseCase.freeUpParkingSpace(idParking, idParkingSpace, idUser)
                .map(ParkingSpaceReservationDtoMapper::toParkingSpaceReleaseResponseDto);
    }
}
