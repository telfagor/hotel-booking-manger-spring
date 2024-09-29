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

CREATE TABLE IF NOT EXISTS user_detail
(
    id UUID PRIMARY KEY,
    phone_number VARCHAR(64) UNIQUE NOT NULL,
    photo VARCHAR(128),
    birthdate DATE NOT NULL,
    money INT NOT NULL DEFAULT 0,
    user_id UUID UNIQUE NOT NULL REFERENCES "user" (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS apartment
(
    id UUID PRIMARY KEY,
    rooms INT NOT NULL,
    seats INT NOT NULL,
    daily_cost INT NOT NULL,
    type VARCHAR(28) NOT NULL,
    photo VARCHAR(128) NOT NULL
);

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


CREATE UNIQUE INDEX user_email_idx ON "user" (email);

CREATE INDEX user_password_idx ON "user" (password);

CREATE INDEX user_role_idx ON "user" (role);

CREATE INDEX apartment_price_idx ON apartment (daily_cost);

CREATE INDEX apartment_type_idx ON apartment (type);

CREATE INDEX rooms_idx ON apartment (rooms);

CREATE INDEX seats_idx ON apartment (seats);

CREATE INDEX status_idx ON "order" (status);

CREATE INDEX user_id_idx ON "order" (user_id);




