package inditex.infrastructure.handler.price.get;

import inditex.domain.price.GetPriceService;
import inditex.infrastructure.grpc.price.get.GetPriceGrpc;
import inditex.infrastructure.grpc.price.get.GetPriceRequest;
import inditex.infrastructure.grpc.price.get.GetPriceResponse;
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
        getPriceService
            .price(handlerMapper.toGetPriceFilter(request))
            .map(handlerMapper::toGetPriceResponse)
            .doOnNext(response -> {
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            });
    }
}
