USE LEM;

DROP TABLE IF EXISTS scrap;
CREATE TABLE scrap
(
    id           int PRIMARY KEY AUTO_INCREMENT,
    serialNumber varchar(64),
    type         varchar(64),
    name         varchar(64),
    spec         varchar(64),
    manufacture  varchar(64),
    scrapDate datetime,
    approver     varchar(64)
);

