CREATE TABLE IF NOT EXISTS spring_app.books (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CLOCK_TIMESTAMP(),
    updated_at TIMESTAMP WITH TIME ZONE,
    version BIGINT NOT NULL DEFAULT 1,
    name VARCHAR(100) NOT NULL,
    publication_date DATE NOT NULL,
    number_of_pages INTEGER NOT NULL,
    genre TEXT NOT NULL DEFAULT 'NA',
    channel TEXT NOT NULL DEFAULT 'NA',
    CONSTRAINT unique_book UNIQUE (name, publication_date, number_of_pages)
);
