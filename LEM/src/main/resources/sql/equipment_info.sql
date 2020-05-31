USE mysql;

# CREATE DATABASE LEM;
USE LEM;

DROP TABLE IF EXISTS equipment;

CREATE TABLE equipment
(
    id           int PRIMARY KEY AUTO_INCREMENT,
    serialNumber varchar(64),
    type         varchar(64),
    name         varchar(64),
    spec         varchar(64),
    unitPrice    Double,
    manufacture  varchar(64),
    purchaseDate datetime,
    manager      varchar(64),
    eState       int
);

-- ----------------------------
-- Records of equipment
-- ----------------------------
insert into equipment
values (1, 'A0001', 'Quanta200', '环境扫描电子显微镜', '显微镜', 3500.0, 'MS', 20190709132500, 'James', 1);
insert into equipment
values (2, 'A0002', 'S-4800', '光学显微镜', '显微镜', 1500.0, 'TW', 20190809140000, 'Elliot', 1);
insert into equipment
values (3, 'A0003', 'TM3000', '台式扫描电镜', '显微镜', 2500.0, 'TW', 20190809140000, 'Amy', 1);
insert into equipment
values (4, 'B0001', 'noran', '能谱仪', '精密仪器', 2500.0, 'MS', 20190809140000, 'Amy', 1);
insert into equipment
values (5, 'B0002', 'Smartlab TM 9KW', 'X射线衍射仪', '精密仪器', 5500.0, 'MS', 20190809140000, 'Elliot', 1);
insert into equipment
values (6, 'B0003', 'MIC-3500', '比表面及孔隙分析仪', '精密仪器', 2500.0, 'TW', 20190609140000, 'Elliot', 1);
insert into equipment
values (7, 'B0004', 'BELSOR-MAX', '比表面积与孔径测定仪', '精密仪器', 7500.0, 'XZ', 20190810140000, 'Elliot', 1);
insert into equipment
values (8, 'B0005', 'BELSORP-MINI', '全自动比表面和孔隙分析仪', '精密仪器', 8500.0, 'CC', 20190509140000, 'Elliot', 1);
insert into equipment
values (9, 'C0001', 'STM-MIX', '烧杯', '普通实验仪器', 10.0, 'XZ', 20180810140000, 'Elliot', 1);
insert into equipment
values (10, 'C0002', 'BLM-MINX', '量筒', '普通实验仪器', 10.0, 'CC', 20170810130000, 'Elliot', 1);

