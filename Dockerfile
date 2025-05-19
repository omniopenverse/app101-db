FROM postgres:15.10

COPY init-db/init-db.sh /docker-entrypoint-initdb.d