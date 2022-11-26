package inditex.infrastructure.configuration;

import inditex.infrastructure.handler.price.get.GetPriceHandler;
import inditex.infrastructure.handler.status.get.GetStatusHandler;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfiguration {

    @Value("${server.port}")
    private int serverPort;

    @Bean
    public Server grpcServer(
        GetStatusHandler getStatusHandler,
        GetPriceHandler getPriceHandler
    ) {
        return
            ServerBuilder
                .forPort(serverPort)
                .addService(getStatusHandler)
                .addService(getPriceHandler)
                .build();
    }
}
