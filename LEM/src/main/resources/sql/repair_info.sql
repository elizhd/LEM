USE mysql;

# CREATE DATABASE LEM;
USE LEM;

DROP TABLE IF EXISTS repairInfo;

CREATE TABLE repairInfo
(
    id           int PRIMARY KEY AUTO_INCREMENT,
    serialNumber  varchar(64),  # 设备号
    name          varchar(64),  #设备名
    spec         varchar(64),   #型号
    repairFactory varchar(64),  #修理厂商
    repairCost    Double,       #修理价格
    repairDate    datetime,     #修理日期
    responsible   varchar(64),  #责任人
    eState       int            #状态 1-正常 2-维修

);

-- ----------------------------
-- Records of repairInfo
-- ----------------------------
