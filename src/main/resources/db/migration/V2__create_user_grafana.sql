CREATE USER grafanareader WITH PASSWORD '';
GRANT CONNECT ON DATABASE spring TO grafanareader;
GRANT USAGE ON SCHEMA spring_app TO grafanareader;
GRANT SELECT ON ALL TABLES IN SCHEMA spring_app TO grafanareader;
ALTER DEFAULT PRIVILEGES IN SCHEMA spring_app GRANT SELECT ON TABLES TO grafanareader;
