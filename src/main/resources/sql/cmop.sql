# SQL-Front 5.1  (Build 4.16)

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;


# Host: localhost    Database: cmop
# ------------------------------------------------------
# Server version 5.5.9

DROP DATABASE IF EXISTS `cmop`;
CREATE DATABASE `cmop` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `cmop`;

#
# Source for table apply
#

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
  KEY `fk_apply_user1` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='资源申请表';

#
# Dumping data for table apply
#

LOCK TABLES `apply` WRITE;
/*!40000 ALTER TABLE `apply` DISABLE KEYS */;
INSERT INTO `apply` VALUES (1,6,'ECS',1,'wenlp-ECS-20120731112233','具体的用途描述信息，具体的用途描述信息，具体的用途描述信息，具体的用途描述信息，具体的用途描述信息，','2012-07-01','2012-07-31','2012-07-01 10:00:00',1,0,NULL,NULL);
INSERT INTO `apply` VALUES (2,5,'ECS',1,'wenlp-ECS-20120710235400','sdfsd','2012-07-11','2012-08-11','2012-07-11 00:14:37',1,0,NULL,NULL);
INSERT INTO `apply` VALUES (3,5,'ES3',1,'wenlp-ES3-20120711001551','sdfsd','2012-07-11','2012-08-11','2012-07-11 00:16:20',4,3,NULL,NULL);
/*!40000 ALTER TABLE `apply` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table audit
#

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
  KEY `fk_audit_audit_flow1` (`audit_flow_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='审批表';

#
# Dumping data for table audit
#

LOCK TABLES `audit` WRITE;
/*!40000 ALTER TABLE `audit` DISABLE KEYS */;
INSERT INTO `audit` VALUES (6,3,3,'2012-07-11 00:17:50','','');
INSERT INTO `audit` VALUES (7,3,1,'2012-07-11 00:34:24','1','');
INSERT INTO `audit` VALUES (8,3,2,'2012-07-11 00:36:26','1','');
/*!40000 ALTER TABLE `audit` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table audit_flow
#

DROP TABLE IF EXISTS `audit_flow`;
CREATE TABLE `audit_flow` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '审批流程ID',
  `flow_type` int(11) NOT NULL COMMENT '流程类型：1-资源申请/变更的审批流程',
  `user_id` int(11) NOT NULL COMMENT '审批人',
  `audit_order` int(11) NOT NULL DEFAULT '0' COMMENT '审批顺序：1-直属领导审批；2...-上级领导审批',
  `is_final` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为终审人：1-是；0-否',
  PRIMARY KEY (`id`),
  KEY `fk_audit_flow_user1` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='审批流程表';

#
# Dumping data for table audit_flow
#

LOCK TABLES `audit_flow` WRITE;
/*!40000 ALTER TABLE `audit_flow` DISABLE KEYS */;
INSERT INTO `audit_flow` VALUES (1,1,2,2,0);
INSERT INTO `audit_flow` VALUES (2,1,3,3,1);
INSERT INTO `audit_flow` VALUES (3,1,4,1,0);
/*!40000 ALTER TABLE `audit_flow` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table compute_item
#

DROP TABLE IF EXISTS `compute_item`;
CREATE TABLE `compute_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `identifier` varchar(45) NOT NULL COMMENT '唯一标识，由后台生成，规则：？',
  `os_type` int(11) NOT NULL COMMENT '操作系统类型：1-Windwos2003R2；2-Windwos2008R2；3-Centos5.6；4-Centos6.3',
  `os_bit` int(11) NOT NULL COMMENT '操作系统位数：1-32bit；2-64bit',
  `server_type` int(11) NOT NULL COMMENT '服务器类型：1-Small；2-Middle；3-Large',
  PRIMARY KEY (`id`),
  KEY `fk_compute_item_apply1` (`apply_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='计算资源（实例）明细表';

#
# Dumping data for table compute_item
#

LOCK TABLES `compute_item` WRITE;
/*!40000 ALTER TABLE `compute_item` DISABLE KEYS */;
INSERT INTO `compute_item` VALUES (13,2,'yVkZ0DgoPkFV',1,1,1);
INSERT INTO `compute_item` VALUES (14,2,'TWaEvaIXnvwO',2,1,1);
INSERT INTO `compute_item` VALUES (15,2,'GamrL548ZmpX',1,1,2);
INSERT INTO `compute_item` VALUES (16,2,'oZ6XwckDG2fd',1,1,2);
INSERT INTO `compute_item` VALUES (17,2,'dX65zvgREfNI',3,1,2);
INSERT INTO `compute_item` VALUES (18,2,'3epHboleElDd',3,1,2);
INSERT INTO `compute_item` VALUES (19,2,'xof8DaYQKTBr',1,1,3);
INSERT INTO `compute_item` VALUES (20,2,'liebCW7ZIIuw',1,1,3);
INSERT INTO `compute_item` VALUES (21,2,'kCnBT1atHwaA',1,1,3);
INSERT INTO `compute_item` VALUES (22,2,'ctGwhSo5tJd5',4,1,3);
INSERT INTO `compute_item` VALUES (23,2,'GzgndXe49KPI',4,1,3);
INSERT INTO `compute_item` VALUES (24,2,'B0zr0clNqL0F',4,1,3);
/*!40000 ALTER TABLE `compute_item` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table compute_storage_item
#

DROP TABLE IF EXISTS `compute_storage_item`;
CREATE TABLE `compute_storage_item` (
  `compute_item_id` int(11) NOT NULL COMMENT '计算资源ID',
  `storage_item_id` int(11) NOT NULL COMMENT '存储资源ID',
  PRIMARY KEY (`compute_item_id`,`storage_item_id`),
  KEY `fk_compute_storage_item_compute_item1` (`compute_item_id`),
  KEY `fk_compute_storage_item_storage_item1` (`storage_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='计算资源与存储资源关联表';

#
# Dumping data for table compute_storage_item
#

LOCK TABLES `compute_storage_item` WRITE;
/*!40000 ALTER TABLE `compute_storage_item` DISABLE KEYS */;
INSERT INTO `compute_storage_item` VALUES (15,1);
INSERT INTO `compute_storage_item` VALUES (16,1);
/*!40000 ALTER TABLE `compute_storage_item` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table fault
#

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
  KEY `fk_fault_user1` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='故障申报表';

#
# Dumping data for table fault
#

LOCK TABLES `fault` WRITE;
/*!40000 ALTER TABLE `fault` DISABLE KEYS */;
INSERT INTO `fault` VALUES (1,1,'1',3,'1','2012-07-07 10:46:02',NULL,NULL);
INSERT INTO `fault` VALUES (2,1,'2',3,'sd','2012-07-07 10:47:14',NULL,NULL);
INSERT INTO `fault` VALUES (3,1,'1',3,'1','2012-07-07 14:52:58',NULL,NULL);
INSERT INTO `fault` VALUES (4,5,'sdf',2,'sdf','2012-07-10 23:23:50',NULL,NULL);
INSERT INTO `fault` VALUES (5,5,'sdfwe',5,'sdfse','2012-07-10 23:24:02',NULL,NULL);
INSERT INTO `fault` VALUES (6,5,'sdfwew',3,'sdf','2012-07-10 23:24:11',NULL,NULL);
INSERT INTO `fault` VALUES (7,5,'sf',3,'sf','2012-07-10 23:31:02',NULL,NULL);
/*!40000 ALTER TABLE `fault` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table group
#

DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '组ID',
  `name` varchar(20) NOT NULL COMMENT '角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='角色（组）表';

#
# Dumping data for table group
#

LOCK TABLES `group` WRITE;
/*!40000 ALTER TABLE `group` DISABLE KEYS */;
INSERT INTO `group` VALUES (1,'管理员');
INSERT INTO `group` VALUES (2,'申请人');
INSERT INTO `group` VALUES (3,'审批人');
/*!40000 ALTER TABLE `group` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table group_permission
#

DROP TABLE IF EXISTS `group_permission`;
CREATE TABLE `group_permission` (
  `group_id` int(11) NOT NULL COMMENT '角色ID',
  `permission` varchar(45) NOT NULL COMMENT '权限',
  KEY `fk_group_permission_group1` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-权限关系表';

#
# Dumping data for table group_permission
#

LOCK TABLES `group_permission` WRITE;
/*!40000 ALTER TABLE `group_permission` DISABLE KEYS */;
INSERT INTO `group_permission` VALUES (1,'user:view');
INSERT INTO `group_permission` VALUES (1,'user:edit');
INSERT INTO `group_permission` VALUES (1,'group:view');
INSERT INTO `group_permission` VALUES (1,'group:edit');
INSERT INTO `group_permission` VALUES (2,'apply:view');
INSERT INTO `group_permission` VALUES (2,'apply:edit');
INSERT INTO `group_permission` VALUES (3,'audit:view');
INSERT INTO `group_permission` VALUES (3,'audit:edit');
/*!40000 ALTER TABLE `group_permission` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table in_vpn_item
#

DROP TABLE IF EXISTS `in_vpn_item`;
CREATE TABLE `in_vpn_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `in_type` int(11) NOT NULL COMMENT '接入类型，根据所选资源类型取值：1-生成资源对应VPN接入；2-测试公测资源对应XenApp接入',
  `account` varchar(45) NOT NULL COMMENT 'VPN账号',
  `account_user` varchar(45) NOT NULL COMMENT '使用人',
  `visit_host` varchar(45) NOT NULL COMMENT '需要访问主机',
  PRIMARY KEY (`id`),
  KEY `fk_in_vpn_item_apply1` (`apply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='VPN接入服务明细表';

#
# Dumping data for table in_vpn_item
#

LOCK TABLES `in_vpn_item` WRITE;
/*!40000 ALTER TABLE `in_vpn_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `in_vpn_item` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table network_domain_item
#

DROP TABLE IF EXISTS `network_domain_item`;
CREATE TABLE `network_domain_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `analyse_type` int(11) NOT NULL COMMENT '解析类型：1-NS；2-MX；3-A；4-CNAME',
  `domain` varchar(45) NOT NULL COMMENT '要解析的完整域名',
  `ip` varchar(45) NOT NULL COMMENT '目标IP地址',
  PRIMARY KEY (`id`),
  KEY `fk_network_domain_item_apply1` (`apply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网络域名解析明细表';

#
# Dumping data for table network_domain_item
#

LOCK TABLES `network_domain_item` WRITE;
/*!40000 ALTER TABLE `network_domain_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `network_domain_item` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table network_port_item
#

DROP TABLE IF EXISTS `network_port_item`;
CREATE TABLE `network_port_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `service_port` varchar(50) NOT NULL COMMENT '服务-端口，如：FTP-21',
  PRIMARY KEY (`id`),
  KEY `fk_network_port_apply1` (`apply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网络开放端口明细表';

#
# Dumping data for table network_port_item
#

LOCK TABLES `network_port_item` WRITE;
/*!40000 ALTER TABLE `network_port_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `network_port_item` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table storage_item
#

DROP TABLE IF EXISTS `storage_item`;
CREATE TABLE `storage_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `identifier` varchar(45) NOT NULL DEFAULT '' COMMENT '唯一标识，由后台生成，规则：？',
  `storage_space` int(11) NOT NULL COMMENT '存储空间：以GB为单位，如：20GB',
  PRIMARY KEY (`id`),
  KEY `fk_storage_item_apply1` (`apply_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='存储资源明细表';

#
# Dumping data for table storage_item
#

LOCK TABLES `storage_item` WRITE;
/*!40000 ALTER TABLE `storage_item` DISABLE KEYS */;
INSERT INTO `storage_item` VALUES (1,3,'',55);
/*!40000 ALTER TABLE `storage_item` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table user
#

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='用户表';

#
# Dumping data for table user
#

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','admin','admin','1',1,NULL,'1','2012-07-01 12:00:00',NULL,1);
INSERT INTO `user` VALUES (2,'zhangpeng','zhangpeng','zhangpeng','1',1,3,'3','2012-07-01 12:00:00',NULL,1);
INSERT INTO `user` VALUES (3,'chenlu','chenlu','chenlu','1',1,NULL,'3','2012-07-01 12:00:00',NULL,1);
INSERT INTO `user` VALUES (4,'zhangmi','zhangmi','zhangmi','1',1,2,'3','2012-07-01 12:00:00',NULL,1);
INSERT INTO `user` VALUES (5,'wenlp','wenlp','wenlp','1',1,4,'2','2012-07-01 12:00:00',NULL,1);
INSERT INTO `user` VALUES (6,'user','user','user','1',1,3,'2','2012-07-01 12:00:00',NULL,1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table user_group
#

DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `group_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`group_id`),
  KEY `fk_user_group_group1` (`group_id`),
  KEY `fk_user_group_user1` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-角色关系表';

#
# Dumping data for table user_group
#

LOCK TABLES `user_group` WRITE;
/*!40000 ALTER TABLE `user_group` DISABLE KEYS */;
INSERT INTO `user_group` VALUES (1,1);
INSERT INTO `user_group` VALUES (2,3);
INSERT INTO `user_group` VALUES (3,3);
INSERT INTO `user_group` VALUES (4,3);
INSERT INTO `user_group` VALUES (5,2);
/*!40000 ALTER TABLE `user_group` ENABLE KEYS */;
UNLOCK TABLES;

#
#  Foreign keys for table apply
#

ALTER TABLE `apply`
ADD CONSTRAINT `fk_apply_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

#
#  Foreign keys for table audit
#

ALTER TABLE `audit`
ADD CONSTRAINT `fk_audit_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_audit_audit_flow1` FOREIGN KEY (`audit_flow_id`) REFERENCES `audit_flow` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

#
#  Foreign keys for table audit_flow
#

ALTER TABLE `audit_flow`
ADD CONSTRAINT `fk_audit_flow_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

#
#  Foreign keys for table compute_item
#

ALTER TABLE `compute_item`
ADD CONSTRAINT `fk_compute_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

#
#  Foreign keys for table compute_storage_item
#

ALTER TABLE `compute_storage_item`
ADD CONSTRAINT `fk_compute_storage_item_compute_item1` FOREIGN KEY (`compute_item_id`) REFERENCES `compute_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_compute_storage_item_storage_item1` FOREIGN KEY (`storage_item_id`) REFERENCES `storage_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

#
#  Foreign keys for table fault
#

ALTER TABLE `fault`
ADD CONSTRAINT `fk_fault_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

#
#  Foreign keys for table group_permission
#

ALTER TABLE `group_permission`
ADD CONSTRAINT `fk_group_permission_group1` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

#
#  Foreign keys for table in_vpn_item
#

ALTER TABLE `in_vpn_item`
ADD CONSTRAINT `fk_in_vpn_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

#
#  Foreign keys for table network_domain_item
#

ALTER TABLE `network_domain_item`
ADD CONSTRAINT `fk_network_domain_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

#
#  Foreign keys for table network_port_item
#

ALTER TABLE `network_port_item`
ADD CONSTRAINT `fk_network_port_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

#
#  Foreign keys for table storage_item
#

ALTER TABLE `storage_item`
ADD CONSTRAINT `fk_storage_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

#
#  Foreign keys for table user_group
#

ALTER TABLE `user_group`
ADD CONSTRAINT `fk_user_group_group1` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_user_group_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;


/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
