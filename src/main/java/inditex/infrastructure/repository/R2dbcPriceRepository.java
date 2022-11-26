package inditex.infrastructure.repository;

import inditex.domain.price.GetPriceFilter;
import inditex.domain.price.Price;
import inditex.domain.price.PriceRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;

import static org.springframework.data.relational.core.query.Criteria.where;

public class R2dbcPriceRepository implements PriceRepository {

    private final R2dbcEntityTemplate entityTemplate;
    private final R2dbcPriceRepositoryMapper mapper;

    public R2dbcPriceRepository(R2dbcEntityTemplate entityTemplate, R2dbcPriceRepositoryMapper mapper) {
        this.entityTemplate = entityTemplate;
        this.mapper = mapper;
    }

    @Override
    public Flux<Price> prices(GetPriceFilter filter) {
        return
            entityTemplate
                .select(PriceDAO.class)
                .from("price")
                .matching(
                    Query
                        .query(
                            where("product_id")
                                .is(filter.productId())
                                .and(
                                    where("brand_id").is(filter.brandId())
                                )
                                .and(
                                    where("start_date").lessThanOrEquals(filter.date())
                                )
                                .and(
                                    where("end_date").greaterThanOrEquals(filter.date())
                                )
                        )
                )
                .all()
                .map(mapper::toPrice);

    }
}
