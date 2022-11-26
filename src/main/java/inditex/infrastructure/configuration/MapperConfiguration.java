package inditex.infrastructure.configuration;

import inditex.infrastructure.handler.price.get.GetPriceHandlerMapper;
import inditex.infrastructure.repository.R2dbcPriceRepositoryMapper;
import inditex.infrastructure.util.validator.RequestParameterValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;

@Configuration
public class MapperConfiguration {

    @Bean
    public GetPriceHandlerMapper getPriceHandlerMapper(
        ZoneId applicationZoneId,
        RequestParameterValidator requestParameterValidator
    ) {
        return new GetPriceHandlerMapper(applicationZoneId, requestParameterValidator);
    }

    @Bean
    public R2dbcPriceRepositoryMapper r2dbcPriceRepositoryMapper() {
        return new R2dbcPriceRepositoryMapper();
    }
}
