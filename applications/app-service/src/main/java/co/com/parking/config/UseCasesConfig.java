package co.com.parking.config;

import co.com.parking.model.parking.gateways.ParkingRepository;
import co.com.parking.usecase.ParkingUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
public class UseCasesConfig {

        @Bean
        public ParkingUseCase parkingUseCase(ParkingRepository parkingRepository) {
                return new ParkingUseCase(parkingRepository);
        }
}
