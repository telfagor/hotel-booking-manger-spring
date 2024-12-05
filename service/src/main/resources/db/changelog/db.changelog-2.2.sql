--liquibase formatted sql

----changeset bolun:add-apartment_number-column-v1
ALTER TABLE apartment
ADD COLUMN apartment_number INT
    UNIQUE NOT NULL GENERATED ALWAYS AS IDENTITY;
--rollback DROP TABLE apartment