--liquibase formatted sql

--changeset bolun:add-apartment_number-column-v1
ALTER TABLE apartment
ADD COLUMN apartment_number INT
    UNIQUE NOT NULL GENERATED BY DEFAULT AS IDENTITY;
--rollback DROP TABLE apartment