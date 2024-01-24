create database shop CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

create user `shop`@`localhost` identified by '1111';
grant all privileges on shop.* to `shop`@`localhost` ;



create table item (
                      price integer not null,
                      stock_number integer not null,
                      item_id bigint not null,
                      reg_time datetime(6),
                      update_time datetime(6),
                      item_nm varchar(50) not null,
                      item_detail tinytext not null,
                      item_sell_status enum ('SELL','SOLD_OUT'),
                      primary key (item_id)
) engine=InnoDB;



ALTER TABLE orders MODIFY COLUMN order_status enum('ORDER','CANCEL') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'ORDER';
