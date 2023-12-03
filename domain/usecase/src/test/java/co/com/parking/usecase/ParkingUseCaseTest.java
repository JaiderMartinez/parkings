package co.com.parking.usecase;

import co.com.parking.model.parking.Parking;
import co.com.parking.model.parking.config.ParkingException;
import co.com.parking.model.parking.gateways.ParkingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingUseCaseTest {

    @Mock
    private ParkingRepository parkingRepository;
    @InjectMocks
    private ParkingUseCase parkingUseCase;

    @Test
    void test_findAll_successful() {
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
    void test_findAll_throwParkingException() {
        when(parkingRepository.findAll()).thenReturn(Flux.empty());
        StepVerifier.create(parkingUseCase.findAll())
                .expectSubscription()
                .expectError(ParkingException.class)
                .verify();
    }
}