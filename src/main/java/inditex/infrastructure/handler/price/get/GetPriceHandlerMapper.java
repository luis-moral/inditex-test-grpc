package inditex.infrastructure.handler.price.get;

import inditex.domain.price.GetPriceFilter;
import inditex.domain.price.Price;
import inditex.infrastructure.grpc.price.get.GetPriceRequest;
import inditex.infrastructure.grpc.price.get.GetPriceResponse;
import inditex.infrastructure.util.validator.RequestParameterValidator;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class GetPriceHandlerMapper {

    private final ZoneId zoneId;
    private final RequestParameterValidator validator;

    public GetPriceHandlerMapper(ZoneId zoneId, RequestParameterValidator validator) {
        this.zoneId = zoneId;
        this.validator = validator;
    }

    public GetPriceFilter toGetPriceFilter(GetPriceRequest request) {
        return
            new GetPriceFilter(
                request.getProductId(),
                request.getBrandId(),
                validator.mandatoryDate(request.getDate(), "date")
            );
    }

    public GetPriceResponse toGetPriceResponse(Price price) {
        return
            GetPriceResponse
                .newBuilder()
                .setId(price.id())
                .setProductId(price.productId())
                .setBrandId(price.brandId())
                .setStartDate(ZonedDateTime.ofInstant(Instant.ofEpochMilli(price.startDate()), zoneId).toOffsetDateTime().toString())
                .setStartDate(ZonedDateTime.ofInstant(Instant.ofEpochMilli(price.endDate()), zoneId).toOffsetDateTime().toString())
                .setPrice(price.price().doubleValue())
                .setCurrency(price.currency().getCurrencyCode())
                .build();
    }
}
