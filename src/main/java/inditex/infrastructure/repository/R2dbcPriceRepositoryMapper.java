package inditex.infrastructure.repository;

import inditex.domain.price.Price;

import java.util.Currency;

public class R2dbcPriceRepositoryMapper {

    public Price toPrice(PriceDAO priceDAO) {
        return
            new Price(
                priceDAO.id(),
                priceDAO.productId(),
                priceDAO.brandId(),
                priceDAO.startDate(),
                priceDAO.endDate(),
                priceDAO.price(),
                Currency.getInstance(priceDAO.currency()),
                priceDAO.priority()
            );
    }
}
