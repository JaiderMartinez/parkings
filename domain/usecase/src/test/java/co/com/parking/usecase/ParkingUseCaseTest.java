package co.com.parking.usecase;

import co.com.parking.model.parking.Parking;
import co.com.parking.model.parking.config.ErrorCode;
import co.com.parking.model.parking.gateways.ParkingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingUseCaseTest {

    @Mock
    private ParkingRepository parkingRepository;
    @InjectMocks
    private ParkingUseCase parkingUseCase;

    @ParameterizedTest(name = "{0}")
    @MethodSource("getValidateRequest")
    void shouldFindAllSuccessTest(final Parking parkingExpected) {
        when(parkingRepository.findAll()).thenReturn(Flux.just(parkingExpected));
        StepVerifier.create(parkingUseCase.findAll())
                .expectSubscription()
                .expectNext(parkingExpected)
                .verifyComplete();
    }

    private static Stream<Arguments> getValidateRequest() {
        return Stream.of(
                Arguments.of( Parking.builder().id(123L).address("Street A").build()),
                Arguments.of( Parking.builder().id(456L).address("Street B").build()),
                Arguments.of( Parking.builder().id(789L).address("Street C").build())
        );
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