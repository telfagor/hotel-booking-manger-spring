--liquibase formatted sql

--changeset bolun:create-revision-table-v1
CREATE TABLE IF NOT EXISTS revision
(
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    timestamp BIGINT NOT NULL
);
--rollback DROP TABLE revision;

--changeset bolun:create-apartment_aud-table-v1
CREATE TABLE IF NOT EXISTS apartment_aud
(
    id UUID,
    rev INT REFERENCES revision (id),
    revtype SMALLINT,
    rooms INT,
    seats INT,
    daily_cost INT,
    type VARCHAR(28),
    photo VARCHAR(128)
);
--rollback DROP TABLE apartment_aud;

--changeset bolun:create-user_aud-table-v1
CREATE TABLE IF NOT EXISTS user_aud (
    id UUID,
    rev INT REFERENCES revision (id),
    revtype SMALLINT,
    first_name VARCHAR(64),
    last_name VARCHAR(64),
    email VARCHAR(64),
    password VARCHAR(128),
    role VARCHAR(28),
    gender VARCHAR(28)
);
--rollback DROP TABLE user_aud;

-- changeset bolun:create-user_detail_aud-table-v1
CREATE TABLE IF NOT EXISTS user_detail_aud (
    id UUID,
    rev INT REFERENCES revision (id),
    revtype SMALLINT,
    phone_number VARCHAR(64),
    photo VARCHAR(128),
    birthdate DATE,
    money INT DEFAULT 0,
    user_id UUID
);
--rollback DROP TABLE user_detail_aud;

-- changeset bolun:create-order_aud-table-v1
CREATE TABLE IF NOT EXISTS order_aud (
    id UUID,
    rev INT REFERENCES revision (id),
    revtype SMALLINT,
    check_in DATE,
    check_out DATE,
    total_cost INT,
    status VARCHAR(64),
    user_id UUID,
    apartment_id UUID
);
--rollback DROP TABLE order_aud;