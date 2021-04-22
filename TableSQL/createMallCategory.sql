CREATE TABLE mall_category(
    id int PRIMARY KEY AUTO_INCREMENT,
    parent_id int DEFAULT NULL,
    name varchar(50) DEFAULT NULL,
    status tinyint DEFAULT 1,
    sort_order int DEFAULT NULL,
    create_time datetime DEFAULT NULL,
    update_time datetime DEFAULT NULL
)AUTO_INCREMENT = 10032;