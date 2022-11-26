package inditex.infrastructure.component;

import io.grpc.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StartGrpcServerOnReady {

    @Autowired
    private Server grpcServer;

    @EventListener(ApplicationReadyEvent.class)
    private void initialize() {
        try {
            grpcServer.start();
            grpcServer.awaitTermination();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
