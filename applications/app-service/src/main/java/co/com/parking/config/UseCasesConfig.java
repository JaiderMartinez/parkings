package co.com.parking.config;

import co.com.parking.model.parking.gateways.ParkingRepository;
import co.com.parking.model.parking.gateways.ParkingSpaceRepository;
import co.com.parking.model.parking.gateways.ReserveSpaceInParkingRepository;
import co.com.parking.usecase.ParkingUseCase;
import co.com.parking.usecase.ReserveParkingSpaceUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

        @Bean
        public ParkingUseCase parkingUseCase(ParkingRepository parkingRepository) {
                return new ParkingUseCase(parkingRepository);
        }

        @Bean
        ReserveParkingSpaceUseCase reserveParkingSpaceUseCase(ParkingSpaceRepository parkingSpaceRepository,
                                                              ReserveSpaceInParkingRepository reserveSpaceInParkingRepository) {
                return new ReserveParkingSpaceUseCase(
                        parkingSpaceRepository,
                        reserveSpaceInParkingRepository);
        }
}
