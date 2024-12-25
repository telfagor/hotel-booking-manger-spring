--liquibase formatted sql

--changeset bolun:add-audit-column-user-table-v1
ALTER TABLE "user"
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(64),
    ADD COLUMN modified_at TIMESTAMP,
    ADD COLUMN modified_by VARCHAR(64);
--rollback ALTER TABLE "user" DROP COLUMN created_at;
--rollback ALTER TABLE "user" DROP COLUMN created_by;
--rollback ALTER TABLE "user" DROP COLUMN modified_at;
--rollback ALTER TABLE "user" DROP COLUMN modified_by;

--chageset bolun:add-audit-column-apartment-table-v1
ALTER TABLE apartment
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(64),
    ADD COLUMN modified_at TIMESTAMP,
    ADD COLUMN modified_by VARCHAR(64);
--rollback ALTER TABLE apartment DROP COLUMN created_at;
--rollback ALTER TABLE apartment DROP COLUMN created_by;
--rollback ALTER TABLE apartment DROP COLUMN modified_at;
--rollback ALTER TABLE apartment DROP COLUMN modified_by;

--changeset bolun:add-audit-column-user_detail-table-v1
ALTER TABLE user_detail
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(64),
    ADD COLUMN modified_at TIMESTAMP,
    ADD COLUMN modified_by VARCHAR(64);
--rollback ALTER TABLE user_detail DROP COLUMN created_at;
--rollback ALTER TABLE user_detail DROP COLUMN created_by;
--rollback ALTER TABLE user_detail DROP COLUMN modified_at;
--rollback ALTER TABLE user_detail DROP COLUMN modified_by;

--changeset bolun:add-audit-column-order-table-v1
ALTER TABLE "order"
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(64),
    ADD COLUMN modified_at TIMESTAMP,
    ADD COLUMN modified_by VARCHAR(64);
--rollback ALTER TABLE "order" DROP COLUMN created_at;
--rollback ALTER TABLE "order" DROP COLUMN created_by;
--rollback ALTER TABLE "order" DROP COLUMN modified_at;
--rollback ALTER TABLE "order" DROP COLUMN modified_by;
