package inditex.infrastructure.handler.status.get;

import inditex.infrastructure.grpc.status.get.GetStatusGrpc;
import inditex.infrastructure.grpc.status.get.GetStatusRequest;
import inditex.infrastructure.grpc.status.get.GetStatusResponse;
import inditex.infrastructure.grpc.status.get.Status;
import io.grpc.stub.StreamObserver;

public class GetStatusHandler extends GetStatusGrpc.GetStatusImplBase {

    @Override
    public void status(
        GetStatusRequest request,
        StreamObserver<GetStatusResponse> responseObserver) {

        GetStatusResponse response =
            GetStatusResponse
                .newBuilder()
                .setStatus(Status.OK)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
