CREATE TABLE mall_product(
    id int PRIMARY KEY  AUTO_INCREMENT,
    category_id int DEFAULT NULL,
    name varchar(100) DEFAULT NULL ,
    subtitle varchar(200) DEFAULT NULL,
    main_image varchar(500) DEFAULT NULL,
    sub_images text,
    detail text,
    price decimal(20,2) DEFAULT NULL ,
    stock int DEFAULT NULL,
    status int DEFAULT 1,
    create_time datetime DEFAULT NULL,
    update_time datetime DEFAULT NULL
)AUTO_INCREMENT=26;