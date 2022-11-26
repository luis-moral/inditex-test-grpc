package inditex.infrastructure.handler.price.get;

import com.google.protobuf.Any;
import com.google.rpc.Code;
import com.google.rpc.ErrorInfo;
import com.google.rpc.Status;
import inditex.domain.price.GetPriceFilter;
import inditex.domain.price.GetPriceService;
import inditex.infrastructure.grpc.price.get.GetPriceGrpc;
import inditex.infrastructure.grpc.price.get.GetPriceRequest;
import inditex.infrastructure.grpc.price.get.GetPriceResponse;
import inditex.infrastructure.util.validator.exception.ValidationException;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;

public class GetPriceHandler extends GetPriceGrpc.GetPriceImplBase {

    private GetPriceService getPriceService;
    private GetPriceHandlerMapper handlerMapper;

    public GetPriceHandler(
        GetPriceService getPriceService,
        GetPriceHandlerMapper handlerMapper
    ) {
        this.getPriceService = getPriceService;
        this.handlerMapper = handlerMapper;
    }

    @Override
    public void price(
        GetPriceRequest request,
        StreamObserver<GetPriceResponse> responseObserver
    ) {
        try {
            GetPriceFilter filter = handlerMapper.toGetPriceFilter(request);

            getPriceService
                .price(filter)
                .map(handlerMapper::toGetPriceResponse)
                .defaultIfEmpty(GetPriceResponse.getDefaultInstance())

                .doOnNext(
                    response -> {
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    })
                .subscribe();
        }
        catch (ValidationException error) {
            handleValidationException(error, responseObserver);
        }
    }

    private void handleValidationException(Throwable error, StreamObserver<GetPriceResponse> responseObserver) {
        Status status =
            Status.newBuilder()
                .setCode(Code.INVALID_ARGUMENT.getNumber())
                .setMessage(error.getLocalizedMessage())
                .addDetails(
                    Any
                        .pack(
                            ErrorInfo
                                .newBuilder()
                                .setReason("Validation Error")
                                .setDomain("inditex")
                                .putMetadata("error", error.getLocalizedMessage())
                                .build()
                        )
                )
                .build();

        responseObserver.onError(StatusProto.toStatusRuntimeException(status));
    }
}
