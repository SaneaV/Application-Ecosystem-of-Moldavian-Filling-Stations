CREATE SEQUENCE IF NOT EXISTS locations_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS locations (
                                         id BIGINT PRIMARY KEY,
                                         town VARCHAR(255),
    district VARCHAR(255),
    country VARCHAR(2),
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    UNIQUE (latitude, longitude)
    );

ALTER TABLE locations ALTER COLUMN id SET DEFAULT NEXT VALUE FOR locations_id_seq;
