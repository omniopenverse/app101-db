#!/bin/bash
set -e

echo "Running init-user-db.sh..."

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE USER app101 WITH PASSWORD 'password';
	CREATE DATABASE app101db WITH ENCODING=‘UTF8’ OWNER=app101;
	GRANT ALL PRIVILEGES ON DATABASE app101db TO app101;

    CREATE TABLE app101db.public.users (
        id SERIAL PRIMARY KEY,
        username VARCHAR(50) NOT NULL,
        email VARCHAR(100)
    );

EOSQL

echo "Database and user created successfully."
