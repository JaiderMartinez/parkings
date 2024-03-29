package co.com.parking.controllers;

import co.com.parking.controllers.dto.request.ParkingRequestDto;
import co.com.parking.controllers.dto.response.ParkingResponseDto;
import co.com.parking.controllers.mapper.ParkingDtoMapper;
import co.com.parking.usecase.ParkingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/parkings")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingUseCase parkingUseCase;

    @GetMapping
    public Flux<ParkingResponseDto> findAll() {
        return parkingUseCase.findAll()
                .map(ParkingDtoMapper::toResponseDto);
    }

    @PostMapping
    public Mono<ParkingResponseDto> save(@RequestBody ParkingRequestDto parkingRequestDto) {
        return parkingUseCase.save(ParkingDtoMapper.toModel(parkingRequestDto))
                .map(ParkingDtoMapper::toResponseDto);
    }

    @GetMapping("/location")
    public Flux<ParkingResponseDto> findByLocation(@RequestParam("latitude") double latitudeInDegrees,
                                                   @RequestParam("longitude")  double longitudeInDegrees) {
        return parkingUseCase.findByLocation(latitudeInDegrees, longitudeInDegrees)
                .map(ParkingDtoMapper::toResponseDto);
    }
}
