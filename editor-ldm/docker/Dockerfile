FROM postgres:15.3

RUN apt-get update \
    && apt-get install -y curl zip unzip build-essential \
    && apt-get install -y netcat-traditional \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

RUN curl -s "https://api.pgxn.org/dist/pgtap/1.2.0/pgtap-1.2.0.zip" --output /tmp/pgtap.zip \
    && unzip /tmp/pgtap.zip -d /opt \
    && cd /opt/pgtap-1.2.0 \
    && make \
    && make install \
    && make clean

COPY postgresql.conf /tmp/postgresql.conf
COPY 0-setup-conf.sh /docker-entrypoint-initdb.d/0-setup-conf.sh
COPY temp/sql/*.sql /docker-entrypoint-initdb.d/

RUN chown postgres /tmp/postgresql.conf
RUN chown -R postgres /docker-entrypoint-initdb.d

RUN localedef -i fr_CA -c -f UTF-8 -A /usr/share/locale/locale.alias fr_CA.UTF-8
ENV LANG fr_CA.utf8

ENV POSTGRES_USER default-user
ENV POSTGRES_PASSWORD default-password
ENV POSTGRES_DB editor-ldm

RUN mkdir /var/log/postgresql/editor-ldm
RUN chown postgres:postgres /var/log/postgresql/editor-ldm

# Add VOLUMEs to allow backup of logs
VOLUME  ["/var/log/postgresql/editor-ldm"]
