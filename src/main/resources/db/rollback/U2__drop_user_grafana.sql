REVOKE SELECT ON ALL TABLES IN SCHEMA spring_app FROM grafanareader;

ALTER DEFAULT PRIVILEGES IN SCHEMA spring_app REVOKE SELECT ON TABLES FROM grafanareader;

REVOKE USAGE ON SCHEMA spring_app FROM grafanareader;
REVOKE CONNECT ON DATABASE spring FROM grafanareader;

REASSIGN OWNED BY grafanareader TO postgres;

DROP OWNED BY grafanareader;
DROP USER IF EXISTS grafanareader;
