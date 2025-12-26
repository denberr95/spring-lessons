CREATE TABLE IF NOT EXISTS spring_app.books (
  id UUID NOT NULL DEFAULT gen_random_uuid(),
  created_at TIMESTAMPTZ NOT NULL DEFAULT clock_timestamp(),
  updated_at TIMESTAMPTZ,
  version BIGINT NOT NULL DEFAULT 1,
  name VARCHAR(100) NOT NULL,
  publication_date DATE NOT NULL,
  number_of_pages INTEGER NOT NULL,
  genre TEXT NOT NULL DEFAULT 'NA',
  channel TEXT NOT NULL DEFAULT 'NA',
  CONSTRAINT pk_books_id PRIMARY KEY (id),
  CONSTRAINT uk_books_name_pub_date_pages UNIQUE (name, publication_date, number_of_pages)
);
