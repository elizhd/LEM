USE mysql;

# CREATE DATABASE LEM;
USE LEM;
DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id       int PRIMARY KEY AUTO_INCREMENT,
    name     varchar(64),
    password varchar(64),
    email    varchar(64),
    role     boolean
);

-- ----------------------------
-- Records of users
-- ----------------------------
insert into user
values (1, 'admin', 'admin', 'admin@live.com', true);
insert into user
values (2, 'elliot', 'elliot', 'elliot@live.com', true);
insert into user
values (3, 'KingVt', 'KingVt', 'KingVt@qq.com', true);
insert into user
values (4, 'FZQ', 'FZQ', 'FZQ@qq.com', false);
insert into user
values (5, 'test', 'test', 'test@qq.com', true);








