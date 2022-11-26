package inditex.infrastructure.configuration;

import inditex.domain.price.GetPriceService;
import inditex.domain.price.PriceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public GetPriceService getPriceService(PriceRepository priceRepository) {
        return new GetPriceService(priceRepository);
    }
}
