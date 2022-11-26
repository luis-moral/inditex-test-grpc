package inditex;

import inditex.infrastructure.grpc.status.get.GetStatusGrpc;
import inditex.infrastructure.grpc.status.get.GetStatusRequest;
import inditex.infrastructure.grpc.status.get.GetStatusResponse;
import inditex.infrastructure.grpc.status.get.Status;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = { "test" })
@SpringBootTest(classes = { Application.class })
public class StatusFeature {

    @Value("${server.port}")
    private int serverPort;

    private ManagedChannel channel;

    @BeforeEach
    public void setUp() {
        channel =
            ManagedChannelBuilder
                .forAddress("localhost", serverPort)
                .usePlaintext()
                .build();
    }

    @Test public void
    can_check_application_status() {
        GetStatusGrpc.GetStatusBlockingStub getStatusStub = GetStatusGrpc.newBlockingStub(channel);
        GetStatusRequest request = GetStatusRequest.newBuilder().build();

        GetStatusResponse expected =
            GetStatusResponse
                .newBuilder()
                .setStatus(Status.OK)
                .build();

        GetStatusResponse response = getStatusStub.status(request);

        Assertions
            .assertThat(response)
            .isEqualTo(expected);
    }
}
