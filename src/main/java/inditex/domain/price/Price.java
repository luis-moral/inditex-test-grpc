package inditex.domain.price;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Currency;

@EqualsAndHashCode
@ToString
public class Price {

    private final long id;
    private final long productId;
    private final int brandId;
    private final long startDate;
    private final long endDate;
    private final BigDecimal price;
    private final Currency currency;
    private final int priority;

    public Price(
        long id,
        long productId,
        int brandId,
        long startDate,
        long endDate,
        BigDecimal price,
        Currency currency,
        int priority
    ) {
        this.id = id;
        this.productId = productId;
        this.brandId = brandId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.currency = currency;
        this.priority = priority;
    }

    public long id() {
        return id;
    }

    public long productId() {
        return productId;
    }

    public int brandId() {
        return brandId;
    }

    public long startDate() {
        return startDate;
    }

    public long endDate() {
        return endDate;
    }

    public BigDecimal price() {
        return price;
    }

    public Currency currency() {
        return currency;
    }

    public int priority() {
        return priority;
    }
}
