--liquibase formatted sql

--changeset bolun:1
ALTER TABLE "user"
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(64),
    ADD COLUMN modified_at TIMESTAMP,
    ADD COLUMN modified_by VARCHAR(64);

ALTER TABLE apartment
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(64),
    ADD COLUMN modified_at TIMESTAMP,
    ADD COLUMN modified_by VARCHAR(64);

ALTER TABLE user_detail
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(64),
    ADD COLUMN modified_at TIMESTAMP,
    ADD COLUMN modified_by VARCHAR(64);

ALTER TABLE "order"
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(64),
    ADD COLUMN modified_at TIMESTAMP,
    ADD COLUMN modified_by VARCHAR(64);
