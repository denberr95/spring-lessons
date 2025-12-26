CREATE SCHEMA IF NOT EXISTS history;

CREATE SEQUENCE history.revinfo_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE history.revinfo (
  rev INTEGER NOT NULL,
  revtstmp BIGINT NOT NULL,
  ip_address VARCHAR(45),
  client_id VARCHAR(255),
  username VARCHAR(255),
  request_uri TEXT,
  http_method VARCHAR(10),
  CONSTRAINT pk_revinfo PRIMARY KEY (rev)
);
