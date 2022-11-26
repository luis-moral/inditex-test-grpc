package inditex.infrastructure.handler.price.get;

import inditex.domain.price.GetPriceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class GetPriceHandler {

    private GetPriceService getPriceService;
    private GetPriceHandlerMapper handlerMapper;

    public GetPriceHandler(
        GetPriceService getPriceService,
        GetPriceHandlerMapper handlerMapper
    ) {
        this.getPriceService = getPriceService;
        this.handlerMapper = handlerMapper;
    }

    public Mono<ServerResponse> price(ServerRequest serverRequest) {
        return
            ServerResponse
                .status(HttpStatus.OK)
                .body(
                    getPriceService
                        .price(handlerMapper.toGetPriceFilter(serverRequest))
                        .map(handlerMapper::toGetPriceResponse),
                    GetPriceResponse.class
                );
    }
}
