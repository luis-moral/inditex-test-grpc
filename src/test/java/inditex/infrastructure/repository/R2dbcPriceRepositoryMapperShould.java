package inditex.infrastructure.repository;

import inditex.domain.price.Price;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

public class R2dbcPriceRepositoryMapperShould {

    private R2dbcPriceRepositoryMapper mapper;
    
    @BeforeEach
    public void setUp() {
        mapper = new R2dbcPriceRepositoryMapper();
    }
    
    @Test public void
    map_price_dao_to_price() {
        PriceDAO priceDAO =
            new PriceDAO(
                1,
                100,
                5,
                150_000L,
                200_000L,
                new BigDecimal("25.5"),
                "EUR",
                3
            );

        Price expected =
            new Price(
                1,
                100,
                5,
                150_000L,
                200_000L,
                new BigDecimal("25.5"),
                Currency.getInstance("EUR"),
                3
            );

        Assertions
            .assertThat(mapper.toPrice(priceDAO))
            .isEqualTo(expected);
    }

}