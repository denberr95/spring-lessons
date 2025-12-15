CREATE TABLE IF NOT EXISTS spring_app.order_items (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  created_at TIMESTAMPTZ NOT NULL DEFAULT clock_timestamp(),
  channel TEXT NOT NULL DEFAULT 'NA'
);
