package inditex.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;

@Configuration
public class SystemConfiguration {

    @Value("${application.date-time.zone-id}")
    private String applicationDateTimeZoneId;

    @Bean
    public ZoneId applicationZoneId() {
        return ZoneId.of(applicationDateTimeZoneId);
    }
}
