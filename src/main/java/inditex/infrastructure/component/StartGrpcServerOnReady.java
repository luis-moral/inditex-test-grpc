package inditex.infrastructure.component;

import io.grpc.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Component
public class StartGrpcServerOnReady {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Server grpcServer;

    @EventListener(ApplicationReadyEvent.class)
    private void initialize() {
        try {
            logger.info("Starting GRPC Server");

            grpcServer.start();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    private void destroy() {
        logger.info("Stopping GRPC Server");

        grpcServer.shutdown();
    }
}
