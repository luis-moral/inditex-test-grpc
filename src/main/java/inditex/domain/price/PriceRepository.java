package inditex.domain.price;

import reactor.core.publisher.Flux;

public interface PriceRepository {

    Flux<Price> prices(GetPriceFilter filter);
}
