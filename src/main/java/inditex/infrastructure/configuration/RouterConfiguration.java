package inditex.infrastructure.configuration;

import inditex.infrastructure.handler.price.get.GetPriceHandler;
import inditex.infrastructure.handler.status.get.GetStatusHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    @Value("${endpoint.status.path}")
    private String statusEndpoint;

    @Value("${endpoint.public.v1.price.path.base}")
    private String priceEndpoint;

    @Bean
    public RouterFunction<ServerResponse> routes(
        GetStatusHandler getStatusHandler,
        GetPriceHandler getPriceHandler
    ) {
        return
            RouterFunctions
                .route(RequestPredicates.GET(statusEndpoint), getStatusHandler::status)
                .andRoute(RequestPredicates.GET(priceEndpoint), getPriceHandler::price);
    }
}
