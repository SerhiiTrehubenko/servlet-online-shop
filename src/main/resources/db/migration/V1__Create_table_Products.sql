CREATE TABLE IF NOT EXISTS products
(
    product_id    BIGSERIAL PRIMARY KEY,
    product_name  VARCHAR(250) UNIQUE NOT NULL,
    product_price DOUBLE PRECISION,
    creationDate   DATE
);