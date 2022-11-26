package inditex.infrastructure.configuration;

import inditex.infrastructure.handler.price.get.GetPriceHandler;
import inditex.infrastructure.handler.status.get.GetStatusHandler;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class GrpcServerConfiguration {

    @Value("${server.port}")
    private int serverPort;

    @Value("${endpoint.status.path}")
    private String statusEndpoint;

    @Value("${endpoint.public.v1.price.path.base}")
    private String priceEndpoint;

    @Bean
    public Server grpcServer(
        GetStatusHandler getStatusHandler,
        GetPriceHandler getPriceHandler
    ) {
        return
            ServerBuilder
                .forPort(serverPort)
                .addService(new HelloServiceImpl())
                .build();
    }
}
