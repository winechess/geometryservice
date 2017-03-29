CREATE DATABASE suumuagent WITH OWNER = postgres ENCODING = 'UTF8' CONNECTION LIMIT = -1;
CREATE USER geometryservice WITH LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION;


CREATE SCHEMA geometryservice;
ALTER SCHEMA geometryservice
OWNER TO geometryservice;
COMMENT ON SCHEMA geometryservice IS 'Schema for geometryservice to store users geodata';

SET search_path = geometryservice, pg_catalog;
SET default_tablespace = '';
SET default_with_oids = false;

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA geometryservice;
CREATE EXTENSION IF NOT EXISTS btree_gist WITH SCHEMA geometryservice;


CREATE TABLE configurations_zones (
  configuration_id BIGINT NOT NULL,
  zone_id          BIGINT NOT NULL
);
ALTER TABLE configurations_zones
  OWNER TO geometryservice;


CREATE SEQUENCE locations_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
ALTER TABLE locations_id_seq
  OWNER TO geometryservice;
CREATE TABLE locations (
  id        BIGINT DEFAULT nextval('locations_id_seq' :: REGCLASS) NOT NULL,
  date      TIMESTAMP WITHOUT TIME ZONE                            NOT NULL,
  device_id BIGINT                                                 NOT NULL,
  point     GEOMETRY(POINT, 4326)                                  NOT NULL
);
ALTER TABLE locations
  OWNER TO geometryservice;


CREATE SEQUENCE zones_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
ALTER TABLE zones_id_seq
  OWNER TO geometryservice;
CREATE TABLE zones (
  id              BIGINT DEFAULT nextval('zones_id_seq' :: REGCLASS) NOT NULL,
  polygon         GEOMETRY(POLYGON, 4326)                            NOT NULL,
  organization_id BIGINT                                             NOT NULL,
  title           CHARACTER VARYING(255)                             NOT NULL,
  created         TIMESTAMP WITHOUT TIME ZONE                        NOT NULL,
  created_by      BIGINT                                             NOT NULL,
  edited          TIMESTAMP WITHOUT TIME ZONE                        NOT NULL,
  edited_by       BIGINT                                             NOT NULL
);
ALTER TABLE zones
  OWNER TO geometryservice;


ALTER TABLE ONLY configurations_zones
  ADD CONSTRAINT configurations_zones_pkey PRIMARY KEY (configuration_id, zone_id);
ALTER TABLE ONLY locations
  ADD CONSTRAINT locations_pkey PRIMARY KEY (id);
ALTER TABLE ONLY zones
  ADD CONSTRAINT zones_pkey PRIMARY KEY (id);


CREATE INDEX locations_device_id_idx
  ON locations USING BTREE (device_id);
CREATE INDEX locations_point_idx
  ON locations USING GIST (point);
CREATE INDEX zones_organization_id_idx
  ON zones USING BTREE (organization_id);
CREATE INDEX zones_polygon_idx
  ON zones USING GIST (polygon);