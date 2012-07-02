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

/*Table structure for table `apply` */

DROP TABLE IF EXISTS `apply`;

CREATE TABLE `apply` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '申请/变更ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `resource_type` int(11) NOT NULL COMMENT '资源类型：1-生产资源；2-测试资源；3-公测资源',
  `title` varchar(20) NOT NULL COMMENT '申请标题',
  `description` varchar(100) NOT NULL COMMENT '申请用途',
  `service_start` varchar(10) NOT NULL COMMENT '服务开始时间',
  `service_end` varchar(10) NOT NULL COMMENT '服务结束时间',
  `create_time` datetime NOT NULL COMMENT '申请/变更时间',
  `server_type` int(11) DEFAULT NULL COMMENT '服务器类型：1-Small；2-Middle；3-Large',
  `server_count` int(11) DEFAULT NULL COMMENT '服务器数量',
  `os_type` int(11) DEFAULT NULL COMMENT '操作系统类型：1-Windwos2003R2；2-Windwos2008R2；3-Centos5.6；4-Centos6.3',
  `os_bit` int(11) DEFAULT NULL COMMENT '操作系统位数：1-32bit；2-64bit',
  `network_type` int(11) DEFAULT NULL COMMENT '网络接入链路：1-电信CTC；2-联通CNC；3-二者',
  `network_band` int(11) DEFAULT NULL COMMENT '网络接入速率：以M为单位，如：1M',
  `network_in_ip` varchar(45) DEFAULT NULL COMMENT '内网IP地址',
  `network_out_ip` varchar(45) DEFAULT NULL COMMENT '外网IP地址，由Redmine反馈，以","分开',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态：1-待审核；2-审核中；3-已审核；4-已退回',
  `redmine_issue_id` int(11) DEFAULT NULL COMMENT '关联的Redmine问题ID',
  `apply_id` int(11) DEFAULT NULL COMMENT '如果是变更记录，则需保存关联的申请ID',
  `redmine_changeset_id` int(11) DEFAULT NULL COMMENT '关联的Redmine变更ID',
  PRIMARY KEY (`id`),
  KEY `fk_apply_user1` (`user_id`),
  CONSTRAINT `fk_apply_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='资源申请表';

/*Data for the table `apply` */

insert  into `apply`(`id`,`user_id`,`resource_type`,`title`,`description`,`service_start`,`service_end`,`create_time`,`server_type`,`server_count`,`os_type`,`os_bit`,`network_type`,`network_band`,`network_in_ip`,`network_out_ip`,`status`,`redmine_issue_id`,`apply_id`,`redmine_changeset_id`) values (1,1,1,'1','1','1','1','2000-00-00 00:00:00',1,1,1,1,1,1,'1','1',1,0,NULL,NULL),(2,1,1,'2','11','1','1','2000-00-00 00:00:00',1,1,1,1,1,1,'1','1',2,NULL,NULL,NULL),(3,1,1,'3','1','1','1','2000-00-00 00:00:00',1,1,1,1,1,1,'1','1',3,NULL,NULL,NULL),(4,1,1,'为mini-web增加分页功能.','3444','2012-07-01','2012-07-01','2012-07-02 16:59:39',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL),(5,1,2,'为mini-web增加分页功能.2','123','2012-07-01','2012-07-02','2012-07-02 17:00:52',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL),(6,1,1,'计算资源申请主题','123','2012-07-06','2012-07-17','2012-07-02 17:27:47',2,20,1,2,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL),(7,1,2,'为mini-web增加分页功能.','2','2012-07-01','2012-07-03','2012-07-02 18:46:57',NULL,NULL,NULL,NULL,1,1,NULL,'1111',1,NULL,NULL,NULL),(8,1,2,'1','111','2012-07-06','2012-07-03','2012-07-02 18:49:24',NULL,NULL,NULL,NULL,1,10,NULL,'1111',1,NULL,NULL,NULL),(9,1,1,'为mini-web增加分页功能.','111','2012-07-06','2012-07-11','2012-07-02 19:26:17',NULL,NULL,NULL,NULL,3,5,NULL,'111',1,NULL,NULL,NULL),(10,1,1,'为mini-web增加分页功能.','111','2012-07-01','2012-07-03','2012-07-02 19:55:42',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL);

/*Table structure for table `audit` */

DROP TABLE IF EXISTS `audit`;

CREATE TABLE `audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '审批ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `audit_flow_id` int(11) NOT NULL,
  `create_time` datetime NOT NULL COMMENT '审批时间',
  `opinion` varchar(45) DEFAULT NULL COMMENT '审批意见',
  PRIMARY KEY (`id`),
  KEY `fk_audit_apply1` (`apply_id`),
  KEY `fk_audit_audit_flow1` (`audit_flow_id`),
  CONSTRAINT `fk_audit_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_audit_audit_flow1` FOREIGN KEY (`audit_flow_id`) REFERENCES `audit_flow` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='审批表';

/*Data for the table `audit` */

/*Table structure for table `audit_flow` */

DROP TABLE IF EXISTS `audit_flow`;

CREATE TABLE `audit_flow` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '审批流程ID',
  `flow_type` int(11) NOT NULL COMMENT '流程类型：1-资源申请/变更的审批流程',
  `user_id` int(11) NOT NULL COMMENT '审批人',
  `audit_order` char(1) NOT NULL COMMENT '审批顺序：1-直属领导审批；2...-上级领导审批',
  PRIMARY KEY (`id`),
  KEY `fk_audit_flow_user1` (`user_id`),
  CONSTRAINT `fk_audit_flow_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='审批流程表';

/*Data for the table `audit_flow` */

/*Table structure for table `fault` */

DROP TABLE IF EXISTS `fault`;

CREATE TABLE `fault` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '申报ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `create_time` datetime NOT NULL COMMENT '申报时间',
  `level` int(11) NOT NULL COMMENT '优先级：1-低；2-普通；3-高；4-紧急；5-立刻',
  `description` varchar(500) NOT NULL COMMENT '故障描述',
  `redmine_issue_id` int(11) DEFAULT NULL COMMENT '关联的Redmine问题ID',
  PRIMARY KEY (`id`),
  KEY `fk_fault_user1` (`user_id`),
  CONSTRAINT `fk_fault_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='故障申报表';

/*Data for the table `fault` */

/*Table structure for table `group` */

DROP TABLE IF EXISTS `group`;

CREATE TABLE `group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='角色（组）表';

/*Data for the table `group` */

insert  into `group`(`id`,`name`) values (1,'管理员'),(2,'用户');

/*Table structure for table `group_permission` */

DROP TABLE IF EXISTS `group_permission`;

CREATE TABLE `group_permission` (
  `id` int(11) NOT NULL,
  `permission` varchar(45) NOT NULL COMMENT '权限',
  KEY `fk_group_permmision_group1` (`id`),
  CONSTRAINT `fk_group_permmision_group1` FOREIGN KEY (`id`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-权限关系表';

/*Data for the table `group_permission` */

insert  into `group_permission`(`id`,`permission`) values (1,'user:view'),(1,'user:edit'),(1,'group:view'),(1,'group:edit'),(2,'user:view'),(2,'group:view');

/*Table structure for table `in_vpn_item` */

DROP TABLE IF EXISTS `in_vpn_item`;

CREATE TABLE `in_vpn_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `in_type` int(11) NOT NULL COMMENT '接入类型，根据所选资源类型取值：1-生成资源对应VPN接入；2-测试公测资源对应XenApp接入',
  `account` varchar(45) NOT NULL COMMENT 'VPN账号',
  `account_user` varchar(45) NOT NULL COMMENT '使用人',
  `visit_host` varchar(45) NOT NULL COMMENT '需要访问主机',
  PRIMARY KEY (`id`),
  KEY `fk_in_vpn_item_apply1` (`apply_id`),
  CONSTRAINT `fk_in_vpn_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='VPN接入服务明细表';

/*Data for the table `in_vpn_item` */

insert  into `in_vpn_item`(`id`,`apply_id`,`in_type`,`account`,`account_user`,`visit_host`) values (1,4,1,'使用人','使用人','需要访问主机'),(2,5,1,'使用人','使用人','需要访问主机');

/*Table structure for table `network_domain_item` */

DROP TABLE IF EXISTS `network_domain_item`;

CREATE TABLE `network_domain_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `analyse_type` int(11) NOT NULL COMMENT '解析类型：1-NS；2-MX；3-A；4-CNAME',
  `domain` varchar(45) NOT NULL COMMENT '要解析的完整域名',
  `ip` varchar(45) NOT NULL COMMENT '目标IP地址',
  PRIMARY KEY (`id`),
  KEY `fk_network_domain_item_apply1` (`apply_id`),
  CONSTRAINT `fk_network_domain_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网络域名解析明细表';

/*Data for the table `network_domain_item` */

/*Table structure for table `network_port_item` */

DROP TABLE IF EXISTS `network_port_item`;

CREATE TABLE `network_port_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `service_port` varchar(50) NOT NULL COMMENT '服务-端口，如：FTP-21',
  PRIMARY KEY (`id`),
  KEY `fk_network_port_apply1` (`apply_id`),
  CONSTRAINT `fk_network_port_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='网络开放端口明细表';

/*Data for the table `network_port_item` */

insert  into `network_port_item`(`id`,`apply_id`,`service_port`) values (1,7,'DNS-32'),(2,7,'Http-80'),(3,7,'222'),(4,8,'FTP-21'),(5,8,'1111,22222,33333'),(6,9,'Http-80'),(7,9,' https-443'),(8,9,'33333');

/*Table structure for table `storage_item` */

DROP TABLE IF EXISTS `storage_item`;

CREATE TABLE `storage_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `storage_type` int(11) NOT NULL COMMENT '存储类型：1-数据存储；2-业务存储',
  `storage_space` int(11) NOT NULL COMMENT '存储空间：以GB为单位，如：20GB',
  `storage_throughput` int(11) NOT NULL COMMENT '存储吞吐量：1-50Mbps以内；2-50Mbps以上',
  `storage_iops` int(11) DEFAULT NULL COMMENT '存储每秒进行读写（I/O）操作的次数',
  PRIMARY KEY (`id`),
  KEY `fk_storage_item_apply1` (`apply_id`),
  CONSTRAINT `fk_storage_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='存储资源明细';

/*Data for the table `storage_item` */

insert  into `storage_item`(`id`,`apply_id`,`storage_type`,`storage_space`,`storage_throughput`,`storage_iops`) values (1,10,1,30,2,100),(2,10,2,100,1,200);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `name` varchar(10) NOT NULL COMMENT '用户姓名',
  `password` varchar(20) NOT NULL COMMENT '用户密码',
  `email` varchar(45) NOT NULL COMMENT 'Sobey邮箱（登录名）',
  `phonenum` varchar(45) NOT NULL COMMENT '联系电话',
  `department` int(11) NOT NULL COMMENT '所属部门ID：1-新媒体产品部；2-新媒体项目部...',
  `leader_id` int(11) DEFAULT NULL COMMENT '直属领导ID',
  `type` char(1) NOT NULL COMMENT '用户类型：1-管理员；2-申请人；3-审核人',
  `create_time` datetime NOT NULL COMMENT '注册时间',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态：1-正常；2-无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`password`,`email`,`phonenum`,`department`,`leader_id`,`type`,`create_time`,`login_time`,`status`) values (1,'admin','admin','admin','1',1,1,'1','2000-00-00 00:00:00','2000-00-00 00:00:00',1),(2,'user','user','user','1',1,1,'1','2000-00-00 00:00:00','2000-00-00 00:00:00',1);

/*Table structure for table `user_group` */

DROP TABLE IF EXISTS `user_group`;

CREATE TABLE `user_group` (
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  KEY `fk_user_group_group1` (`group_id`),
  KEY `fk_user_group_user1` (`user_id`),
  CONSTRAINT `fk_user_group_group1` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_group_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-角色关系表';

/*Data for the table `user_group` */

insert  into `user_group`(`user_id`,`group_id`) values (1,1),(1,2),(2,2);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
