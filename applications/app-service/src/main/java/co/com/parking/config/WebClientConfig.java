package co.com.parking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${endpoint.micro.user}")
    private String baseUrl;

    @Bean("webClientUser")
    public WebClient webClient() {
        return WebClient.builder().baseUrl(baseUrl).build();
    }
}
