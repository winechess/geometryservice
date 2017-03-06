-- CREATE DATABASE suumuagent
-- WITH
-- OWNER = postgres
-- ENCODING = 'UTF8'
-- CONNECTION LIMIT = -1;

-- CREATE USER geometryservice WITH
--   LOGIN
--   NOSUPERUSER
--   INHERIT
--   NOCREATEDB
--   NOCREATEROLE
--   NOREPLICATION;
--
--
-- CREATE SCHEMA IF NOT EXISTS geometryservice AUTHORIZATION geometryservice;
-- ALTER SCHEMA geometryservice OWNER TO geometryservice;
-- COMMENT ON SCHEMA geometryservice IS 'Schema for geometryservice to store users geodata';


-- SET SCHEMA 'geometryservice';


CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA geometryservice;
CREATE EXTENSION IF NOT EXISTS btree_gist WITH SCHEMA geometryservice;


CREATE SEQUENCE IF NOT EXISTS coordinates_id_seq;
ALTER SEQUENCE coordinates_id_seq OWNER TO geometryservice;
CREATE TABLE IF NOT EXISTS coordinates
(
  id        BIGINT                NOT NULL DEFAULT nextval('coordinates_id_seq' :: REGCLASS),
  date      TIMESTAMP WITHOUT TIME ZONE NOT NULL ,
  lat       DOUBLE PRECISION      NOT NULL,
  lng       DOUBLE PRECISION      NOT NULL,
  device_id BIGINT                NOT NULL,
  location  GEOMETRY(POINT, 4326) NOT NULL,

  CONSTRAINT coordinates_pkey PRIMARY KEY (id),
  CONSTRAINT coordinates_lat_check CHECK (lat > '-90' :: INTEGER :: DOUBLE PRECISION AND
                                          lat <= 90 :: DOUBLE PRECISION ),
  CONSTRAINT coordinates_lng_check CHECK (lng > '-180' :: INTEGER :: DOUBLE PRECISION AND
                                          lng <= 180 :: DOUBLE PRECISION )

) WITH (
OIDS = FALSE
);
ALTER TABLE coordinates OWNER TO geometryservice;
CREATE INDEX coordinates_location_idx ON coordinates USING GIST (location) TABLESPACE pg_default;
CREATE INDEX coordinates_device_id_idx ON coordinates(device_id);

