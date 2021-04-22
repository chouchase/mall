CREATE TABLE mall_order(
    id int primary key AUTO_INCREMENT,
    order_no bigint DEFAULT NULL,
    user_id int DEFAULT NULL,
    shipping_id int DEFAULT NULL,
    payment decimal(20,3) DEFAULT NULL,
    payment_type int DEFAULT NULL,
    postage int DEFAULT NULL,
    status int DEFAULT NULL,
    payment_time datetime DEFAULT NULL,
    send_time datetime DEFAULT NULL,
    end_time datetime DEFAULT NULL,
    close_time datetime DEFAULT  NULL,
    create_time datetime DEFAULT NULL,
    update_time datetime DEFAULT NULL,
    UNIQUE KEY order_no_index (order_no) USING BTREE
)AUTO_INCREMENT=103;