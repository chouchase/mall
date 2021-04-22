CREATE TABLE mall_pay_info(
    id int PRIMARY KEY AUTO_INCREMENT,
    user_id int DEFAULT NULL,
    order_no bigint DEFAULT NULL,
    pay_platform int DEFAULT NULL,
    platform_number varchar(200) DEFAULT NULL,
    platform_status varchar(20) DEFAULT NULL,
    payment decimal(20,3),
    create_time datetime NOT NULL ,
    update_time datetime NOT NULL
)AUTO_INCREMENT=53;