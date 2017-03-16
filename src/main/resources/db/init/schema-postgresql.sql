CREATE DATABASE suumuagent
WITH
OWNER = postgres
ENCODING = 'UTF8'
CONNECTION LIMIT = -1;

CREATE USER geometryservice WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION;


CREATE SCHEMA IF NOT EXISTS geometryservice
  AUTHORIZATION geometryservice;
ALTER SCHEMA geometryservice
OWNER TO geometryservice;
COMMENT ON SCHEMA geometryservice IS 'Schema for geometryservice to store users geodata';


SET SCHEMA 'geometryservice';


CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA geometryservice;
CREATE EXTENSION IF NOT EXISTS btree_gist WITH SCHEMA geometryservice;


CREATE SEQUENCE IF NOT EXISTS coordinates_id_seq;
ALTER SEQUENCE coordinates_id_seq
OWNER TO geometryservice;
CREATE TABLE IF NOT EXISTS coordinates
(
  id        BIGINT                      NOT NULL DEFAULT nextval('coordinates_id_seq' :: REGCLASS),
  date      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  lat       DOUBLE PRECISION            NOT NULL,
  lng       DOUBLE PRECISION            NOT NULL,
  device_id BIGINT                      NOT NULL,
  location  GEOMETRY(POINT, 4326)       NOT NULL,

  CONSTRAINT coordinates_pkey PRIMARY KEY (id),
  CONSTRAINT coordinates_lat_check CHECK (lat > '-90' :: INTEGER :: DOUBLE PRECISION AND
                                          lat <= 90 :: DOUBLE PRECISION ),
  CONSTRAINT coordinates_lng_check CHECK (lng > '-180' :: INTEGER :: DOUBLE PRECISION AND
                                          lng <= 180 :: DOUBLE PRECISION )

);
ALTER TABLE coordinates
  OWNER TO geometryservice;
CREATE INDEX coordinates_location_idx
  ON coordinates USING GIST (location) TABLESPACE pg_default;
CREATE INDEX coordinates_device_id_idx
  ON coordinates (device_id);

CREATE SEQUENCE IF NOT EXISTS geometryservice.zones_id_seq;
ALTER SEQUENCE zones_id_seq
OWNER TO geometryservice;
CREATE TABLE IF NOT EXISTS geometryservice.zones
(
  id              BIGINT                      NOT NULL DEFAULT nextval('zones_id_seq' :: REGCLASS),
  title           VARCHAR(255)                NOT NULL,
  polygon         GEOMETRY(POLYGON, 4326)     NOT NULL,
  organization_id BIGINT                      NOT NULL,
  created         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  created_by      BIGINT                      NOT NULL,
  edited          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  edited_by       BIGINT                      NOT NULL,

  CONSTRAINT zones_pkey PRIMARY KEY (id)
);
ALTER TABLE geometryservice.zones
  OWNER TO geometryservice;
CREATE INDEX zones_polygon_idx
  ON zones USING GIST (polygon) TABLESPACE pg_default;
CREATE INDEX zones_config_id_idx
  ON zones (organization_id);


CREATE TABLE IF NOT EXISTS geometryservice.configurations_zones
(
  configuration_id BIGINT NOT NULL,
  zone_id          BIGINT NOT NULL,
  CONSTRAINT configurations_zones_pkey PRIMARY KEY (configuration_id, zone_id)
);

ALTER TABLE geometryservice.configurations_zones
  OWNER TO geometryservice;