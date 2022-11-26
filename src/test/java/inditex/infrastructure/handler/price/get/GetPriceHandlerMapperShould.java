package inditex.infrastructure.handler.price.get;

import inditex.domain.price.GetPriceFilter;
import inditex.domain.price.Price;
import inditex.infrastructure.grpc.price.get.GetPriceRequest;
import inditex.infrastructure.grpc.price.get.GetPriceResponse;
import inditex.infrastructure.util.validator.RequestParameterValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Currency;

public class GetPriceHandlerMapperShould {

    private ZoneId zoneId;
    private RequestParameterValidator validator;

    private GetPriceHandlerMapper mapper;

    @BeforeEach
    public void setUp() {
        zoneId = ZoneId.of("CET");
        validator = Mockito.mock(RequestParameterValidator.class);

        mapper = new GetPriceHandlerMapper(zoneId, validator);
    }

    @Test public void
    map_request_to_get_price_filter() {
        GetPriceRequest request =
            GetPriceRequest
                .newBuilder()
                .setProductId(1500L)
                .setBrandId(5)
                .build();

        GetPriceFilter expected =
            new GetPriceFilter(
                1500,
                5,
                1668863977000L
            );

        Mockito
            .when(validator.mandatoryDate(Mockito.any(), Mockito.eq("date")))
            .thenReturn(1668863977000L);

        Assertions
            .assertThat(mapper.toGetPriceFilter(request))
            .isEqualTo(expected);

        Mockito
            .verify(validator, Mockito.times(1))
            .mandatoryDate(Mockito.any(), Mockito.eq("date"));
    }

    @Test public void
    map_price_to_get_price_response() {
        Price price =
            new Price(
                1L,
                1500L,
                5,
                1668853977000L,
                1668863977000L,
                new BigDecimal("25.5"),
                Currency.getInstance("EUR"),
                0
            );

        GetPriceResponse expected =
            GetPriceResponse
                .newBuilder()
                .setId(1L)
                .setProductId(1500L)
                .setBrandId(5)
                .setStartDate(ZonedDateTime.ofInstant(Instant.ofEpochMilli(1668853977000L), zoneId).toOffsetDateTime().toString())
                .setStartDate(ZonedDateTime.ofInstant(Instant.ofEpochMilli(1668863977000L), zoneId).toOffsetDateTime().toString())
                .setPrice(new BigDecimal("25.5").doubleValue())
                .setCurrency("EUR")
                .build();

        Assertions
            .assertThat(mapper.toGetPriceResponse(price))
            .isEqualTo(expected);
    }
}