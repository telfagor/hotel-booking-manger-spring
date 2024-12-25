--liquibase formatted sql

--changeset bolun:create-persistent_logins-table-v1
CREATE TABLE persistent_logins
(
    username  VARCHAR(64) NOT NULL,
    series    VARCHAR(64) PRIMARY KEY,
    token     VARCHAR(64) NOT NULL,
    last_used TIMESTAMP   NOT NULL
);
--rollback DROP TABLE persistent_logins;