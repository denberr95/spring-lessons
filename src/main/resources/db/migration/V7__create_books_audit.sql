CREATE TABLE history.books_audit (
  id UUID NOT NULL,
  rev INTEGER NOT NULL,
  revtype SMALLINT NOT NULL,
  created_at TIMESTAMPTZ,
  updated_at TIMESTAMPTZ,
  version BIGINT,
  name VARCHAR(100),
  publication_date DATE,
  number_of_pages INTEGER,
  channel VARCHAR(50),
  genre VARCHAR(50),
  CONSTRAINT pk_books_audit_id_rev PRIMARY KEY (id, rev),
  CONSTRAINT fk_books_audit_rev FOREIGN KEY (rev) REFERENCES history.revinfo (rev)
);
