/*
SQLyog Ultimate v8.6 RC2
MySQL - 5.5.21 : Database - cmop
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`cmop` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `cmop`;

/*Table structure for table `acct_group` */

DROP TABLE IF EXISTS `acct_group`;

CREATE TABLE `acct_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `acct_group` */

insert  into `acct_group`(`id`,`name`) values (2,'用户'),(1,'管理员');

/*Table structure for table `acct_group_permission` */

DROP TABLE IF EXISTS `acct_group_permission`;

CREATE TABLE `acct_group_permission` (
  `group_id` bigint(20) NOT NULL,
  `permission` varchar(255) NOT NULL,
  KEY `FKAE243466DE3FB930` (`group_id`),
  CONSTRAINT `FKAE243466DE3FB930` FOREIGN KEY (`group_id`) REFERENCES `acct_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `acct_group_permission` */

insert  into `acct_group_permission`(`group_id`,`permission`) values (1,'user:view'),(1,'user:edit'),(1,'group:view'),(1,'group:edit'),(2,'user:view'),(2,'group:view');

/*Table structure for table `acct_user` */

DROP TABLE IF EXISTS `acct_user`;

CREATE TABLE `acct_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_name` (`login_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `acct_user` */

insert  into `acct_user`(`id`,`email`,`login_name`,`name`,`password`) values (1,'admin@springside.org.cn','admin','admin','admin'),(2,'user@springside.org.cn','user','user','user');

/*Table structure for table `acct_user_group` */

DROP TABLE IF EXISTS `acct_user_group`;

CREATE TABLE `acct_user_group` (
  `user_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  KEY `FKFE85CB3EDE3FB930` (`group_id`),
  KEY `FKFE85CB3E836A7D10` (`user_id`),
  CONSTRAINT `FKFE85CB3E836A7D10` FOREIGN KEY (`user_id`) REFERENCES `acct_user` (`id`),
  CONSTRAINT `FKFE85CB3EDE3FB930` FOREIGN KEY (`group_id`) REFERENCES `acct_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `acct_user_group` */

insert  into `acct_user_group`(`user_id`,`group_id`) values (1,1),(1,2),(2,2);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
