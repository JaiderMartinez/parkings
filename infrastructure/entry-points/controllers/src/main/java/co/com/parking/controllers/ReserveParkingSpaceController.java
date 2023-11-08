package co.com.parking.controllers;

import co.com.parking.controllers.dto.request.ParkingLotOccupyRequestDto;
import co.com.parking.controllers.dto.response.ParkingSpaceReleaseResponseDto;
import co.com.parking.controllers.dto.response.ReserveSpaceResponseDto;
import co.com.parking.controllers.mapper.ParkingSpaceReservationMapper;
import co.com.parking.usecase.ReserveParkingSpaceUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/parking-lot")
@RequiredArgsConstructor
public class ReserveParkingSpaceController {

    private final ReserveParkingSpaceUseCase reserveParkingSpaceUseCase;

    //FIXME como deberia usar el ResponseEntity
    //TODO como es el flujo
    //TODO falta manejar las transacciones
    @PostMapping("/occupy")
    public Mono<ResponseEntity<Mono<ReserveSpaceResponseDto>>> reserveParkingSpace(@RequestBody ParkingLotOccupyRequestDto parkingLotOccupyRequestDto) {
        return Mono.just( new ResponseEntity<>(
                reserveParkingSpaceUseCase.reserveParkingSpace(parkingLotOccupyRequestDto.getParkingId(),
                                                               parkingLotOccupyRequestDto.getIdUser(),
                                                               parkingLotOccupyRequestDto.getIdParkingSpace())
                .map(ParkingSpaceReservationMapper::toResponseDto), HttpStatus.CREATED));
    }

    @PatchMapping("/leave/{idParking}")
    public ResponseEntity<Mono<ParkingSpaceReleaseResponseDto>> freeUpParkingSpace(
            @PathVariable(name = "idParking") Long idParking,
            @RequestParam(name = "idUser") Long idUser) {
        return ResponseEntity.ok(reserveParkingSpaceUseCase.freeUpParkingSpace(idParking, idUser)
                .map(ParkingSpaceReservationMapper::toParkingSpaceReleaseResponseDto));
    }
}
