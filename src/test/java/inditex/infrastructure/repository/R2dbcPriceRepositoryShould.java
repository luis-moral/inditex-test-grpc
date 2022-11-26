package inditex.infrastructure.repository;

import inditex.domain.price.GetPriceFilter;
import inditex.domain.price.Price;
import inditex.test.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class R2dbcPriceRepositoryShould {

    private final static AtomicInteger ID_COUNTER = new AtomicInteger(0);
    private final static ZoneId ZONE_ID = ZoneId.of("CET");

    private final static Price FIRST_PRICE =
        price(1, 35455, 1, "2020-06-14T00:00:00", "2020-12-31T23:59:59", "35.50", 0);
    private final static Price SECOND_PRICE =
        price(2, 35455, 1, "2020-06-14T15:00:00", "2020-06-14T18:30:00", "25.45", 1);

    private R2dbcEntityTemplate entityTemplate;
    private R2dbcPriceRepositoryMapper mapper;

    private R2dbcPriceRepository priceRepository;

    @BeforeEach
    public void setUp() {
        mapper = new R2dbcPriceRepositoryMapper();

        entityTemplate = initTemplate(nextSchema());
        priceRepository = new R2dbcPriceRepository(entityTemplate, mapper);
    }

    @Test public void
    return_prices_filtered() {
        GetPriceFilter filter =
            new GetPriceFilter(
                35455,
                1,
                ZonedDateTime.of(2020, 6, 14, 16, 0, 0, 0, ZONE_ID).toInstant().toEpochMilli()
            );

        StepVerifier
            .create(priceRepository.prices(filter).collectList())
            .assertNext(prices ->
                Assertions
                    .assertThat(prices)
                    .containsExactlyInAnyOrder(FIRST_PRICE, SECOND_PRICE)
            )
            .verifyComplete();
    }

    @Test public void
    return_empty_if_no_prices() {
        GetPriceFilter filter =
            new GetPriceFilter(
                35455,
                1,
                ZonedDateTime.of(2020, 6, 14, 16, 0, 0, 0, ZONE_ID).toInstant().toEpochMilli()
            );

        entityTemplate
            .getDatabaseClient()
            .sql("DELETE FROM price")
            .fetch()
            .all()
            .blockLast();

        StepVerifier
            .create(priceRepository.prices(filter))
            .verifyComplete();
    }

    private R2dbcEntityTemplate initTemplate(String schema) {
        return new R2dbcEntityTemplate(TestUtils.initDatabase(schema));
    }

    private String nextSchema() {
        return R2dbcPriceRepositoryShould.class.getSimpleName() + "_" + ID_COUNTER.getAndIncrement();
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