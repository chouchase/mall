CREATE TABLE mall_shipping(
    id int PRIMARY KEY AUTO_INCREMENT,
    user_id int DEFAULT NULL,
    receiver_name varchar(20) DEFAULT NULL,
    receiver_phone varchar(20) DEFAULT NULL,
    receiver_mobile varchar(20) DEFAULT NULL,
    receiver_province varchar(20) DEFAULT NULL,
    receiver_city varchar(20) DEFAULT NULL,
    receiver_district varchar(20) DEFAULT NULL,
    receiver_address varchar(200) DEFAULT NULL,
    receiver_zip varchar(6) DEFAULT NULL,
    create_time datetime DEFAULT NULL,
    update_time datetime DEFAULT NULL
)AUTO_INCREMENT=32;