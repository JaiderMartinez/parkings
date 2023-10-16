package co.com.parking.controllers;

import co.com.parking.r2dbc.ParkingRepositoryImplementation;
import co.com.parking.r2dbc.entities.ParkingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(path = "/parkings")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingRepositoryImplementation parkingRepositoryImplementation;

    @GetMapping
    private Flux<ParkingEntity> findAll() {
        return parkingRepositoryImplementation.findAll();
    }
}
