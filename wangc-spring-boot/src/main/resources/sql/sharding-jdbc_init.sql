CREATE DATABASE IF NOT EXISTS `sharding-jdbc-db-master`;
CREATE DATABASE IF NOT EXISTS `sharding-jdbc-db-slave`;

use sharding-jdbc-db-master;

CREATE TABLE IF NOT EXISTS `tb_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nickname` VARCHAR(128) DEFAULT NULL,
  `password` VARCHAR(128) DEFAULT NULL,
  `sex` TINYINT(1) DEFAULT NULL,
  `birthday` VARCHAR(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

use sharding-jdbc-db-slave;

CREATE TABLE IF NOT EXISTS `tb_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nickname` VARCHAR(128) DEFAULT NULL,
  `password` VARCHAR(128) DEFAULT NULL,
  `sex` TINYINT(1) DEFAULT NULL,
  `birthday` VARCHAR(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;