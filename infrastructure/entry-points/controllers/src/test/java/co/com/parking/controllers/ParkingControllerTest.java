package co.com.parking.controllers;

import co.com.parking.controllers.dto.response.ParkingResponseDto;
import co.com.parking.controllers.mapper.ParkingDtoMapper;
import co.com.parking.model.parking.Parking;
import co.com.parking.model.parking.gateways.ParkingRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebFluxTest(ParkingController.class)
//@SpringBootTest
@ContextConfiguration(classes = ParkingController.class)
class ParkingControllerTest {

    //@Configuration
    //@ComponentScan(basePackages = "co.com.parking")
    //static class TestConfig {
    //}

    private final static String PATH_PARKING = "/parkings";

    @MockBean
    private ParkingRepository parkingRepository;
    @Autowired
    private WebTestClient webTestClient;


    @ParameterizedTest(name = "{0}")
    @MethodSource("getValidateRequest")
    void test_findAllSuccessful(final Parking parkingExpected) {
        /*Flux<Parking> parkingsExpected = Flux.just(parkingExpected);
        Mockito.when(parkingRepository.findAll()).thenReturn(parkingsExpected);
        URI uri = URI.create(PATH_PARKING);
        /*this.webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ParkingResponseDto.class)
                .contains(ParkingDtoMapper.toResponseDto(parkingExpected));*/
    }

    private static Stream<Arguments> getValidateRequest() {
        return Stream.of(
                Arguments.of(Parking.builder().id(123L).address("Street A").build()),
                Arguments.of(Parking.builder().id(456L).address("Street B").build()),
                Arguments.of(Parking.builder().id(789L).address("Street C").build())
        );
    }
}