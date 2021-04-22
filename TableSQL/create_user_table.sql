CREATE TABLE mall_user(
    id int PRIMARY KEY AUTO_INCREMENT,
    username varchar(50) NOT NULL ,
    password varchar(50) NOT NULL ,
    email varchar(50) DEFAULT NULL,
    phone varchar(20) DEFAULT NULL,
    question varchar(100) DEFAULT NULL,
    answer varchar(100) DEFAULT NULL,
    role int DEFAULT NULL,
    create_time datetime DEFAULT NULL ,
    update_time datetime DEFAULT NULL,
    UNIQUE KEY idx_username (username)
)AUTO_INCREMENT=21;