CREATE TABLE IF NOT EXISTS spring_app.items (
  id UUID NOT NULL DEFAULT gen_random_uuid(),
  created_at TIMESTAMPTZ NOT NULL DEFAULT clock_timestamp(),
  price NUMERIC(6, 2) NOT NULL,
  name VARCHAR(100) NOT NULL,
  barcode VARCHAR(50) NOT NULL,
  order_items_id UUID NOT NULL,
  CONSTRAINT pk_items_id PRIMARY KEY (id),
  CONSTRAINT uk_items_barcode UNIQUE (barcode),
  CONSTRAINT fk_order_items_id FOREIGN KEY (order_items_id) REFERENCES spring_app.order_items (id)
);
