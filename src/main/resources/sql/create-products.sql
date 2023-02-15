CREATE TABLE IF NOT EXISTS products
(
    product_id    BIGSERIAL PRIMARY KEY,
    product_name  VARCHAR(250),
    product_price DOUBLE PRECISION,
    creationDate   DATE
);