package co.com.parking.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
@WebFluxTest(ReserveParkingSpaceController.class)
class ReserveParkingSpaceControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void reserveParkingSpace() {
    }

    @Test
    void freeUpParkingSpace() {
    }
}