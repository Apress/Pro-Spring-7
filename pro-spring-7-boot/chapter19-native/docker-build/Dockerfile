FROM mariadb:latest

ENV MARIADB_ROOT_PASSWORD=mainpass

ENV MARIADB_USER=prospring7
ENV MARIADB_PASSWORD=prospring7
ENV MARIADB_DATABASE=musicdb

# 'docker-entrypoint-initdb.d' is the enforced entry point file name, even with podman
COPY scripts /docker-entrypoint-initdb.d/
# user and group are called 'mysql' even if we are using mariadb
RUN chown -R mysql:mysql /docker-entrypoint-initdb.d/

ENV PORT=3306
EXPOSE 3306
