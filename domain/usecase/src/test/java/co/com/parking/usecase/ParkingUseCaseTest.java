package co.com.parking.usecase;

import co.com.parking.model.parking.Parking;
import co.com.parking.model.parking.config.ErrorCode;
import co.com.parking.model.parking.gateways.ParkingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingUseCaseTest {

    @Mock
    private ParkingRepository parkingRepository;
    @InjectMocks
    private ParkingUseCase parkingUseCase;

    @Test
    void shouldFindAllSuccessTest() {
        Parking parking = Parking.builder()
                        .id(1L)
                        .name("name")
                        .address("address")
                        .hourPrice(1000)
                        .build();
        when(parkingRepository.findAll()).thenReturn(Flux.just(parking));
        StepVerifier.create(parkingUseCase.findAll())
                .expectSubscription()
                .expectNext(parking)
                .verifyComplete();
    }

    @Test
    void findAllExceptionNotFoundTest() {
        when(parkingRepository.findAll()).thenReturn(Flux.empty());
        StepVerifier.create(parkingUseCase.findAll())
                .expectSubscription()
                .expectErrorMessage(ErrorCode.S204000.getLog())
                .verify();
    }

    @Test
    void shouldSaveSuccessfulTest() {
        Parking parking = Parking.builder()
                .id(1L)
                .name("name")
                .address("address")
                .hourPrice(1000)
                .build();
        when(parkingRepository.save(parking)).thenReturn(Mono.just(parking));
        StepVerifier.create(parkingUseCase.save(parking))
                .expectSubscription()
                .expectNext(parking)
                .verifyComplete();
    }
}