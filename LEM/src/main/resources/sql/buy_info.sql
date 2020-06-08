USE LEM;
DROP TABLE IF EXISTS buy;

CREATE TABLE buy
(
    id           int PRIMARY KEY AUTO_INCREMENT,
    serialNumber varchar(64),
    type         varchar(64),
    name         varchar(64),
    spec         varchar(64),
    unitPrice    Double,
    manufacture  varchar(64),
    applyDate   datetime,
    approver   varchar(64)
);
# insert into buy
#  values (1, 'A0001', 'Quanta200', '环境扫描电子显微镜', '显微镜', 3500.0, 'MS', 20190709132500,'James');
# insert into buy
# values (2, 'A0002', 'Quanta201', '环境扫描电子显微镜', '显微镜', 3500.0, 'MS', 20190709132500,'hcy');