CREATE SCHEMA geometryservice
  AUTHORIZATION geometryservice;
COMMENT ON SCHEMA geometryservice IS 'Schema for geometryservice to store users geodata';

CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE coordinates
(
  id        BIGINT                NOT NULL DEFAULT nextval('coordinates_id_seq' :: REGCLASS),
  date      TIMESTAMP WITHOUT TIME ZONE,
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


ALTER TABLE coordinates OWNER TO suumu;
CREATE INDEX coordinates_location_idx ON coordinates USING GIST (location) TABLESPACE pg_default;
CREATE INDEX coordinates_device_id_idx ON coordinates(device_id);