package co.com.parking.controllers;

import co.com.parking.controllers.dto.response.ParkingResponseDto;
import co.com.parking.controllers.util.Constants;
import co.com.parking.model.parking.Parking;
import co.com.parking.model.parking.gateways.ParkingRepository;
import co.com.parking.model.parking.gateways.UserGateway;
import co.com.parking.usecase.ParkingUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient
@SpringBootTest
class ParkingControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private ParkingRepository parkingRepository;
    @MockBean
    private UserGateway userGateway;

    @Test
    void test_findAll_successful() {
        Parking parking = Parking.builder()
                .id(1L)
                .name("name")
                .address("address")
                .hourPrice(1000)
                .build();
        when(parkingRepository.findAll()).thenReturn(Flux.just(parking));
        webTestClient.get()
                .uri(Constants.PATH_PARKING)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ParkingResponseDto.class);
                /*.consumeWith( response -> {
                    //Assertions -> podemos añadir cualquier validacion
                })
                 */
                //.hasSize(9);
    }
}