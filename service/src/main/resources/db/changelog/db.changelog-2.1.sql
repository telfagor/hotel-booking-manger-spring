--liquibase formatted sql

--changeset bolun:1
CREATE TABLE IF NOT EXISTS revision
(
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    timestamp BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS apartment_aud
(
    id BIGINT,
    rev INT REFERENCES revision (id),
    revtype SMALLINT,
    username VARCHAR(64) NOT NULL UNIQUE,
    birth_date DATE,
    firstname VARCHAR(64),
    lastname VARCHAR(64),
    role VARCHAR(32)
);