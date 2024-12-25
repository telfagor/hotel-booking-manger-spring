--liquibase formatted sql

--changeset bolun:add-user-deleted-column-v1
ALTER TABLE "user"
ADD COLUMN deleted BOOLEAN DEFAULT FALSE NOT NULL;
--rollback ALTER TABLE "user" DROP COLUMN deleted;

--changeset bolun:add-apartment-deleted-column-v1
ALTER TABLE apartment
ADD COLUMN deleted BOOLEAN DEFAULT FALSE NOT NULL;
--rollback ALTER TABLE apartment DROP COLUMN deleted;

--changeset bolun:add-order-deleted-column-v1
ALTER TABLE "order"
ADD COLUMN deleted BOOLEAN DEFAULT FALSE NOT NULL;
--rollback ALTER TABLE "order" DROP COLUMN deleted;



