CREATE TABLE SPRING_APP.books (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    name VARCHAR(100) NOT NULL,
    publication_date DATE NOT NULL,
    number_of_pages INTEGER NOT NULL,
    CONSTRAINT unique_book UNIQUE (name, publication_date, number_of_pages)
);
