package co.com.parking.r2dbc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "resilience.communication")
public class RetryConfig {

    private long maxAttempts;
    private long backoffInterval;
}
