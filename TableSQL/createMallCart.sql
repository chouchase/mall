CREATE TABLE mall_cart(
    id int PRIMARY KEY AUTO_INCREMENT,
    user_id int DEFAULT NULL,
    product_id int DEFAULT NULL,
    quantity int DEFAULT NULL,
    checked int DEFAULT NULL,
    create_time datetime DEFAULT NULL ,
    update_time datetime DEFAULT NULL,
    KEY idx_user_id (user_id)
)AUTO_INCREMENT=121;