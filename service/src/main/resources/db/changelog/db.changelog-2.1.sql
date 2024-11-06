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
    id BIGINT,
    rev INT REFERENCES revision (id),
    revtype SMALLINT,
    username VARCHAR(64) NOT NULL UNIQUE,
    birth_date DATE,
    firstname VARCHAR(64),
    lastname VARCHAR(64),
    role VARCHAR(32)
);
--rollback DROP TABLE apartment_aud;

--changeset bolun:create-user_aud-table-v1
CREATE TABLE IF NOT EXISTS user_aud (
    id UUID,
    rev INT REFERENCES revision (id),
    revtype SMALLINT,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    email VARCHAR(64) UNIQUE NOT NULL,
    password VARCHAR(128) NOT NULL,
    role VARCHAR(28) NOT NULL,
    gender VARCHAR(28) NOT NULL
);
--rollback DROP TABLE user_aud;

-- changeset bolun:create-user_detail_aud-table-v1
CREATE TABLE IF NOT EXISTS user_detail_aud (
    id UUID,
    rev INT REFERENCES revision (id),
    revtype SMALLINT,
    phone_number VARCHAR(64) UNIQUE NOT NULL,
    photo VARCHAR(128),
    birthdate DATE NOT NULL,
    money INT NOT NULL DEFAULT 0,
    user_id UUID REFERENCES "user" (id) ON DELETE CASCADE
);
--rollback DROP TABLE user_detail_aud;

-- changeset bolun:create-order_aud-table-v1
CREATE TABLE IF NOT EXISTS order_aud (
    id UUID,
    rev INT REFERENCES revision (id),
    revtype SMALLINT,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    total_cost INT NOT NULL,
    status VARCHAR(64) NOT NULL,
    user_id UUID REFERENCES "user" (id),
    apartment_id UUID REFERENCES apartment (id)
);
--rollback DROP TABLE order_aud;