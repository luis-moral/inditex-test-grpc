package inditex.infrastructure.configuration;

import inditex.domain.price.PriceRepository;
import inditex.infrastructure.repository.R2dbcPriceRepository;
import inditex.infrastructure.repository.R2dbcPriceRepositoryMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public PriceRepository priceRepository(
        R2dbcEntityTemplate r2dbcEntityTemplate,
        R2dbcPriceRepositoryMapper r2dbcPriceRepositoryMapper
    ) {
        return new R2dbcPriceRepository(r2dbcEntityTemplate, r2dbcPriceRepositoryMapper);
    }
}
