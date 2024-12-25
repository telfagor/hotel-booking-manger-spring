--liquibase formatted sql

--changeset bolun:create-user-table-v1
CREATE TABLE IF NOT EXISTS "user"
(
    id UUID PRIMARY KEY,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    email VARCHAR(64) UNIQUE NOT NULL,
    password VARCHAR(128) NOT NULL,
    role VARCHAR(28) NOT NULL DEFAULT 'USER',
    gender VARCHAR(28) NOT NULL
);
--rollback DROP TABLE "user";

--changeset bolun:create-user_detail-table-v1
CREATE TABLE IF NOT EXISTS user_detail
(
    id UUID PRIMARY KEY,
    phone_number VARCHAR(64) UNIQUE NOT NULL,
    photo VARCHAR(128),
    birthdate DATE NOT NULL,
    money INT NOT NULL DEFAULT 0,
    user_id UUID UNIQUE NOT NULL REFERENCES "user" (id) ON DELETE CASCADE
);
--rollback DROP TABLE user_detail;

--changeset bolun:create-apartment-table-v1
CREATE TABLE IF NOT EXISTS apartment
(
    id UUID PRIMARY KEY,
    rooms INT NOT NULL,
    seats INT NOT NULL,
    daily_cost INT NOT NULL,
    type VARCHAR(28) NOT NULL,
    photo VARCHAR(128) NOT NULL
);
--rollback DROP TABLE apartment;

--changeset bolun:create-order-table-v1
CREATE TABLE IF NOT EXISTS "order"
(
    id UUID PRIMARY KEY,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    total_cost INT NOT NULL,
    status VARCHAR(64) NOT NULL,
    user_id UUID NOT NULL REFERENCES "user" (id),
    apartment_id UUID NOT NULL REFERENCES apartment (id)
);
--rollback DROP TABLE "order";

--changeset bolun:create-apartment-price-index-v1
CREATE INDEX IF NOT EXISTS apartment_price_idx ON apartment (daily_cost);
--rollback DROP INDEX apartment_price_idx;

--changeset bolun:create-apartment-type-index-v1
CREATE INDEX IF NOT EXISTS apartment_type_idx ON apartment (type);
--rollback DROP INDEX apartment_type_idx;

--changeset bolun:create-apartment-rooms-index-v1
CREATE INDEX IF NOT EXISTS rooms_idx ON apartment (rooms);
--rollback DROP INDEX rooms_idx;

--changeset bolun:create-apartment-seats-index-v1
CREATE INDEX IF NOT EXISTS seats_idx ON apartment (seats);
--rollback DROP INDEX seats_idx;

--changeset bolun:create-apartment-status-index-v1
CREATE INDEX IF NOT EXISTS status_idx ON "order" (status);
--rollback DROP INDEX status_idx;

--changeset bolun:create-user-detail-user-id-index-v1
CREATE INDEX IF NOT EXISTS user_detail_user_id_idx ON user_detail (user_id);
--rollback DROP INDEX user_detail_user_id_idx;

--changeset bolun:create-order-user-id-index-v1
CREATE INDEX IF NOT EXISTS order_user_id_idx ON "order" (user_id);
--rollback DROP INDEX order_user_id_idx;

--changeset bolun:create-apartment-id-index-v1
CREATE INDEX IF NOT EXISTS order_apartment_id_idx ON "order" (apartment_id);
--rollback DROP INDEX order_apartment_id_idx;