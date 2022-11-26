package inditex.infrastructure.component;

import io.grpc.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PreDestroy;
import java.time.Duration;

@Component
public class ApplicationRunnerComponent implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Server grpcServer;

    private Disposable serverKeepAlive;

    @Override
    public void run(ApplicationArguments arguments) throws Exception {
        logger.info("Starting - GRPC Server");

        grpcServer.start();

        logger.info("Started - GRPC Server at port=[{}]", grpcServer.getPort());

        serverKeepAlive =
            Flux
                .interval(Duration.ofMillis(500))
                .subscribeOn(Schedulers.newSingle("ServerKeepAlive"))
                .subscribe();
    }

    @PreDestroy
    public void onExit() {
        logger.info("Stopping - GRPC Server");

        serverKeepAlive.dispose();
        grpcServer.shutdown();

        logger.info("Stopped - GRPC Server");

        serverKeepAlive = null;
    }

}
