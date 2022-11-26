package inditex.infrastructure.repository;

import java.math.BigDecimal;

public record PriceDAO(
    long id,
    long productId,
    int brandId,
    long startDate,
    long endDate,
    BigDecimal price,
    String currency,
    int priority
) {}
