CREATE TABLE price (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    product_id BIGINT NOT NULL,
    brand_id INT NOT NULL,
    start_date BIGINT NOT NULL,
    end_date BIGINT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    priority INT NOT NULL
);
CREATE INDEX index_product_id_brand_id_start_date_end_date ON price (product_id, brand_id, start_date, end_date);