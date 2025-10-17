CREATE TABLE IF NOT EXISTS spring_app.order_items (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
  quantity INTEGER NOT NULL DEFAULT 0,
  channel TEXT NOT NULL DEFAULT 'NA',
  status TEXT NOT NULL DEFAULT 'NA'
);
