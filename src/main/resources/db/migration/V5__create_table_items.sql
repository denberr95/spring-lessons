CREATE TABLE IF NOT EXISTS spring_app.items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CLOCK_TIMESTAMP(),
    updated_at TIMESTAMP WITH TIME ZONE,
    price NUMERIC(6, 2) NOT NULL,
    name VARCHAR(100) NOT NULL,
    barcode VARCHAR(50) NOT NULL,
    fk_order_items_id UUID NOT NULL,
    CONSTRAINT unique_barcode UNIQUE (barcode),
    CONSTRAINT fk_order_items_id FOREIGN KEY (fk_order_items_id) REFERENCES spring_app.order_items(id)
);
