package inditex.domain.price;

import reactor.core.publisher.Mono;

import java.util.Comparator;

public class GetPriceService {

    private final PriceRepository priceRepository;

    public GetPriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public Mono<Price> price(GetPriceFilter filter) {
        return
            priceRepository
                .prices(filter)
                .sort(Comparator.comparingInt(Price::priority).reversed())
                .next();
    }
}
