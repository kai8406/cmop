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
  `service_type` varchar(45) NOT NULL COMMENT '服务类型：ECS、ES3...，若是组合，以“-”隔开',
  `resource_type` int(11) NOT NULL COMMENT '资源类型：1-生产资源；2-测试资源；3-公测资源',
  `title` varchar(45) NOT NULL COMMENT '标题，自动生成规则：用户-服务类型-创建时间（YYYYMMDDHHMMSS）',
  `description` varchar(100) NOT NULL COMMENT '用途描述',
  `service_start` varchar(10) NOT NULL COMMENT '服务开始时间',
  `service_end` varchar(10) NOT NULL COMMENT '服务结束时间',
  `create_time` datetime NOT NULL COMMENT '申请/变更时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态：1-待审核；2-审核中；3-已审核；4-已退回',
  `audit_order` int(11) NOT NULL DEFAULT '0' COMMENT '当前审批顺序，同审批流程表中的audit_order',
  `redmine_issue_id` int(11) DEFAULT NULL COMMENT '关联Redmine的ID',
  `apply_id` int(11) DEFAULT NULL COMMENT '如果是变更记录，则需保存关联的申请ID',
  PRIMARY KEY (`id`),
  KEY `fk_apply_user1` (`user_id`),
  CONSTRAINT `fk_apply_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源申请表';

/*Data for the table `apply` */

/*Table structure for table `audit` */

DROP TABLE IF EXISTS `audit`;

CREATE TABLE `audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '审批ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `audit_flow_id` int(11) NOT NULL,
  `create_time` datetime NOT NULL COMMENT '审批时间',
  `result` char(1) NOT NULL COMMENT '审批结果：1-同意；2-不同意但继续；3-不同意且退回',
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
  `audit_order` int(11) NOT NULL DEFAULT '0' COMMENT '审批顺序：1-直属领导审批；2...-上级领导审批',
  `is_final` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为终审人：1-是；0-否',
  PRIMARY KEY (`id`),
  KEY `fk_audit_flow_user1` (`user_id`),
  CONSTRAINT `fk_audit_flow_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='审批流程表';

/*Data for the table `audit_flow` */

insert  into `audit_flow`(`id`,`flow_type`,`user_id`,`audit_order`,`is_final`) values (1,1,2,2,0),(2,1,3,3,1),(3,1,4,1,0);

/*Table structure for table `compute_item` */

DROP TABLE IF EXISTS `compute_item`;

CREATE TABLE `compute_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `identifier` varchar(45) NOT NULL COMMENT '唯一标识，由后台生成，规则：？',
  `os_type` int(11) NOT NULL COMMENT '操作系统类型：1-Windwos2003R2；2-Windwos2008R2；3-Centos5.6；4-Centos6.3',
  `os_bit` int(11) NOT NULL COMMENT '操作系统位数：1-32bit；2-64bit',
  `server_type` int(11) NOT NULL COMMENT '服务器类型：1-Small；2-Middle；3-Large',
  PRIMARY KEY (`id`),
  KEY `fk_compute_item_apply1` (`apply_id`),
  CONSTRAINT `fk_compute_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='计算资源（实例）明细表';

/*Data for the table `compute_item` */

/*Table structure for table `compute_storage_item` */

DROP TABLE IF EXISTS `compute_storage_item`;

CREATE TABLE `compute_storage_item` (
  `compute_item_id` int(11) NOT NULL COMMENT '计算资源ID',
  `storage_item_id` int(11) NOT NULL COMMENT '存储资源ID',
  PRIMARY KEY (`compute_item_id`,`storage_item_id`),
  KEY `fk_compute_storage_item_compute_item1` (`compute_item_id`),
  KEY `fk_compute_storage_item_storage_item1` (`storage_item_id`),
  CONSTRAINT `fk_compute_storage_item_compute_item1` FOREIGN KEY (`compute_item_id`) REFERENCES `compute_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_compute_storage_item_storage_item1` FOREIGN KEY (`storage_item_id`) REFERENCES `storage_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='计算资源与存储资源关联表';

/*Data for the table `compute_storage_item` */

/*Table structure for table `fault` */

DROP TABLE IF EXISTS `fault`;

CREATE TABLE `fault` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '申报ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `title` varchar(45) NOT NULL COMMENT '标题，自动生成规则：用户-故障申报-创建时间（YYYYMMDDHHMMSS）',
  `level` int(11) NOT NULL COMMENT '优先级：1-低；2-普通；3-高；4-紧急；5-立刻',
  `description` varchar(500) NOT NULL COMMENT '故障描述',
  `create_time` datetime NOT NULL COMMENT '申报时间',
  `redmine_issue_id` int(11) DEFAULT NULL COMMENT '关联Redmine的ID',
  `redmine_status` int(11) DEFAULT NULL COMMENT 'Redmine中的状态：1-已受理；2-已解决',
  PRIMARY KEY (`id`),
  KEY `fk_fault_user1` (`user_id`),
  CONSTRAINT `fk_fault_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='故障申报表';

/*Data for the table `fault` */

/*Table structure for table `group` */

DROP TABLE IF EXISTS `group`;

CREATE TABLE `group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '组ID',
  `name` varchar(20) NOT NULL COMMENT '角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='角色（组）表';

/*Data for the table `group` */

insert  into `group`(`id`,`name`) values (1,'管理员'),(2,'申请人'),(3,'审批人');

/*Table structure for table `group_permission` */

DROP TABLE IF EXISTS `group_permission`;

CREATE TABLE `group_permission` (
  `group_id` int(11) NOT NULL COMMENT '角色ID',
  `permission` varchar(45) NOT NULL COMMENT '权限',
  KEY `fk_group_permission_group1` (`group_id`),
  CONSTRAINT `fk_group_permission_group1` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-权限关系表';

/*Data for the table `group_permission` */

insert  into `group_permission`(`group_id`,`permission`) values (2,'apply:view'),(2,'apply:edit'),(3,'audit:view'),(3,'audit:edit'),(1,'user:view'),(1,'user:edit'),(1,'group:view'),(1,'group:edit'),(1,'apply:view'),(1,'apply:edit'),(1,'audit:view'),(1,'audit:edit');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='VPN接入服务明细表';

/*Data for the table `in_vpn_item` */

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网络开放端口明细表';

/*Data for the table `network_port_item` */

/*Table structure for table `storage_item` */

DROP TABLE IF EXISTS `storage_item`;

CREATE TABLE `storage_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `identifier` varchar(45) NOT NULL DEFAULT '' COMMENT '唯一标识，由后台生成，规则：？',
  `space` int(11) NOT NULL COMMENT '存储空间：以GB为单位，如：20GB',
  `storage_type` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_storage_item_apply1` (`apply_id`),
  CONSTRAINT `fk_storage_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='存储资源明细表';

/*Data for the table `storage_item` */

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
  `type` int(1) NOT NULL COMMENT '用户类型：1-管理员；2-申请人；3-审核人',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态：1-正常；2-无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`password`,`email`,`phonenum`,`department`,`leader_id`,`type`,`create_time`,`login_time`,`status`) values (1,'admin','admin','admin','1',1,NULL,1,'2012-07-01 12:00:00',NULL,1),(2,'zhangpeng','zhangpeng','zhangpeng','1',1,3,3,'2012-07-01 12:00:00',NULL,1),(3,'chenlu','chenlu','chenlu','1',1,NULL,3,'2012-07-01 12:00:00',NULL,1),(4,'zhangmi','zhangmi','zhangmi','1',1,2,3,'2012-07-01 12:00:00',NULL,1),(5,'wenlp','wenlp','wenlp','1',1,4,2,'2012-07-23 16:49:00',NULL,1);

/*Table structure for table `user_group` */

DROP TABLE IF EXISTS `user_group`;

CREATE TABLE `user_group` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `group_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`group_id`),
  KEY `fk_user_group_group1` (`group_id`),
  KEY `fk_user_group_user1` (`user_id`),
  CONSTRAINT `fk_user_group_group1` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_group_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-角色关系表';

/*Data for the table `user_group` */

insert  into `user_group`(`user_id`,`group_id`) values (1,1),(5,2),(2,3),(3,3);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
