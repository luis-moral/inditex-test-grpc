package inditex.domain.price;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.List;

public class GetPriceServiceShould {

    private final static ZoneId ZONE_ID = ZoneId.of("CET");

    private final static Price FIRST_PRICE =
        price(1, 35455, 1, "2020-06-14T00:00:00", "2020-12-31T23:59:59", "35.50", 0);
    private final static Price SECOND_PRICE =
        price(2, 35455, 1, "2020-06-14T15:00:00", "2020-06-14T18:30:00", "25.45", 1);
    private final static Price THIRD_PRICE =
        price(3, 35455, 1, "2020-06-15T00:00:00", "2020-06-15T11:00:00", "30.50", 1);
    private final static Price FOURTH_PRICE =
        price(4, 35455, 1, "2020-06-15T16:00:00", "2020-12-31T23:59:59", "38.95", 1);

    private PriceRepository priceRepository;

    private GetPriceService getPriceService;

    @BeforeEach
    public void setUp() {
        priceRepository = Mockito.mock(PriceRepository.class);

        getPriceService = new GetPriceService(priceRepository);
    }

    @Test public void
    return_the_price_for_a_filtered_product() {
        assertPriceFor(filter(14, 10), List.of(FIRST_PRICE), FIRST_PRICE);
        assertPriceFor(filter(14, 16), List.of(FIRST_PRICE, SECOND_PRICE), SECOND_PRICE);
        assertPriceFor(filter(14, 21), List.of(FIRST_PRICE), FIRST_PRICE);
        assertPriceFor(filter(15, 10), List.of(FIRST_PRICE, THIRD_PRICE), THIRD_PRICE);
        assertPriceFor(filter(16, 21), List.of(FIRST_PRICE, FOURTH_PRICE), FOURTH_PRICE);
    }

    @Test public void
    return_empty_if_no_products_match() {
        GetPriceFilter filter =
            new GetPriceFilter(25, 7, ZonedDateTime.of(2020, 6, 14, 16, 0, 0, 0, ZONE_ID).toInstant().toEpochMilli());

        Mockito
            .when(priceRepository.prices(filter))
            .thenReturn(Flux.empty());

        StepVerifier
            .create(getPriceService.price(filter))
            .verifyComplete();

        Mockito
            .verify(priceRepository, Mockito.times(1))
            .prices(filter);
    }

    private void assertPriceFor(GetPriceFilter filter, List<Price> repositoryPrices, Price expected) {
        Mockito
            .when(priceRepository.prices(filter))
            .thenReturn(Flux.fromIterable(repositoryPrices));

        StepVerifier
            .create(getPriceService.price(filter))
            .expectNext(expected)
            .verifyComplete();

        Mockito
            .verify(priceRepository, Mockito.times(1))
            .prices(filter);
    }

    private GetPriceFilter filter(int dayOfMonth, int hour) {
        return
            new GetPriceFilter(
                35455,
                1,
                ZonedDateTime.of(2020, 6, dayOfMonth, hour, 0, 0, 0, ZONE_ID).toInstant().toEpochMilli()
            );
    }

    private static Price price(
        long id,
        long productId,
        int brandId,
        String startDate,
        String endDate,
        String price,
        int priority
    ) {
        return
            new Price(
                id,
                productId,
                brandId,
                LocalDateTime.parse(startDate).atZone(ZONE_ID).toInstant().toEpochMilli(),
                LocalDateTime.parse(endDate).atZone(ZONE_ID).toInstant().toEpochMilli(),
                new BigDecimal(price),
                Currency.getInstance("EUR"),
                priority
            );
    }
}