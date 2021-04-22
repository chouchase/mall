CREATE TABLE mall_order_item(
    id int PRIMARY KEY AUTO_INCREMENT,
    user_id int DEFAULT NULL,
    order_no bigint DEFAULT NULL,
    product_id int DEFAULT NULL,
    product_name varchar(100) DEFAULT NULL,
    product_image varchar(500) DEFAULT NULL,
    current_unit_price decimal(20,2) DEFAULT NULL,
    quantity int DEFAULT NULL,
    total_price DECIMAL(20,2) DEFAULT NULL,
    create_time datetime DEFAULT NULL,
    update_time datetime DEFAULT NULL,
    key order_no_index (order_no) USING BTREE ,
    key order_no_user_id_index (user_id,order_no) USING BTREE
)AUTO_INCREMENT=113;