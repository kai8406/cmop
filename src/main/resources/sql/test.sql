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

/*Table structure for table `application` */

DROP TABLE IF EXISTS `application`;

CREATE TABLE `application` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `compute_item_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL COMMENT '应用名称',
  `version` varchar(45) NOT NULL COMMENT '版本',
  `deploy_path` varchar(45) NOT NULL COMMENT '部署路径',
  PRIMARY KEY (`id`),
  KEY `fk_application_compute_item1_idx` (`compute_item_id`),
  CONSTRAINT `fk_application_compute_item1` FOREIGN KEY (`compute_item_id`) REFERENCES `compute_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用表';

/*Data for the table `application` */

/*Table structure for table `apply` */

DROP TABLE IF EXISTS `apply`;

CREATE TABLE `apply` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '申请/变更ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `resource_type` int(11) NOT NULL COMMENT '资源类型：1-生产资源；2-测试资源；3-公测资源',
  `title` varchar(45) NOT NULL COMMENT '标题，自动生成规则：用户-服务类型-创建时间（YYYYMMDDHHMMSS）',
  `description` varchar(500) NOT NULL COMMENT '用途描述',
  `service_start` varchar(10) NOT NULL COMMENT '服务开始时间',
  `service_end` varchar(10) NOT NULL COMMENT '服务结束时间',
  `create_time` datetime NOT NULL COMMENT '申请/变更时间',
  `status` int(11) NOT NULL COMMENT '状态：0-已申请；1-待审核；2-审核中；3-已退回；4-已审核；5-处理中；6-已创建',
  `audit_flow_id` int(11) DEFAULT NULL COMMENT '当前所在审批流程ID',
  `zone` varchar(45) DEFAULT NULL COMMENT '所在区域',
  `apply_id` int(11) DEFAULT NULL COMMENT '如果是变更记录，则需保存关联的申请ID',
  `redmine_issue_id` int(11) DEFAULT NULL COMMENT '关联Redmine的ID',
  `priority` int(11) DEFAULT NULL COMMENT '优先级：2-普通；3-高；4-紧急',
  `service_type` int(11) DEFAULT NULL COMMENT '大服务类型：1-基础设施；2-MDN；3-云生产；4-监控',
  PRIMARY KEY (`id`),
  KEY `fk_apply_user1_idx` (`user_id`),
  KEY `fk_apply_redmine_issue1_idx` (`redmine_issue_id`),
  KEY `fk_apply_audit_flow1_idx` (`audit_flow_id`),
  CONSTRAINT `fk_apply_audit_flow1` FOREIGN KEY (`audit_flow_id`) REFERENCES `audit_flow` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_apply_redmine_issue1` FOREIGN KEY (`redmine_issue_id`) REFERENCES `redmine_issue` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_apply_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='资源申请表（也同样适合于服务变更，变更时先向此表写入一行新的记录，再向各个单项写入新的记录）';

/*Data for the table `apply` */

insert  into `apply`(`id`,`user_id`,`resource_type`,`title`,`description`,`service_start`,`service_end`,`create_time`,`status`,`audit_flow_id`,`zone`,`apply_id`,`redmine_issue_id`,`priority`,`service_type`) values (1,11,1,'liuliming-apply-bsp-20130109170045','1','2013-01-09','2013-07-09','2013-01-09 17:00:45',0,NULL,NULL,NULL,NULL,NULL,NULL),(2,11,1,'liuliming-apply-monitor-20130109170349','12','2013-01-09','2013-07-09','2013-01-09 17:03:49',6,2,NULL,NULL,NULL,4,4),(3,11,1,'liuliming-apply-monitor-20130109170406','12','2013-01-09','2013-07-09','2013-01-09 17:04:06',4,2,NULL,NULL,NULL,4,4),(4,11,1,'liuliming-apply-monitor-20130110105416','121','2013-01-10','2013-07-10','2013-01-10 10:54:16',6,2,NULL,NULL,NULL,4,4),(5,11,1,'liuliming-apply-monitor-20130110105623','121212','2013-01-10','2013-07-10','2013-01-10 10:56:23',1,1,NULL,NULL,NULL,4,4),(6,11,1,'liuliming-apply-monitor-20130110110125','1212','2013-01-10','2013-07-10','2013-01-10 11:01:25',6,2,NULL,NULL,NULL,4,4),(16,11,1,'liuliming-apply-monitor-20130115140741','1212','2013-01-15','2013-07-15','2013-01-15 14:07:41',6,2,NULL,NULL,NULL,4,4),(18,11,1,'liuliming-apply-monitor-20130115154621','12121','2013-01-15','2013-07-15','2013-01-15 15:46:21',6,2,NULL,NULL,NULL,4,4),(19,11,1,'liuliming-apply-monitor-20130115155634','222222','2013-01-15','2013-07-15','2013-01-15 15:56:34',6,2,NULL,NULL,NULL,4,4),(20,11,1,'liuliming-apply-monitor-20130115163014','12','2013-01-15','2013-07-15','2013-01-15 16:30:14',0,NULL,NULL,NULL,NULL,4,4),(21,11,1,'liuliming-apply-monitor-20130117101248','22','2013-01-16','2013-07-17','2013-01-17 10:12:48',0,NULL,NULL,NULL,NULL,4,4),(22,11,1,'liuliming-apply-monitor-20130117101946','1212','2013-01-17','2013-07-17','2013-01-17 10:19:46',0,NULL,NULL,NULL,NULL,4,4),(23,11,1,'liuliming-apply-monitor-20130117103532','12','2013-01-17','2013-07-17','2013-01-17 10:35:32',0,NULL,NULL,NULL,NULL,4,4),(24,11,1,'liuliming-apply-monitor-20130117104303','1212','2013-01-17','2013-07-17','2013-01-17 10:43:03',0,NULL,NULL,NULL,NULL,4,4),(25,11,1,'liuliming-apply-20130117112700','121','2013-01-17','2013-07-17','2013-01-17 11:27:00',6,2,NULL,NULL,NULL,NULL,1),(26,11,1,'liuliming-apply-monitor-20130117112836','1212','2013-01-17','2013-07-17','2013-01-17 11:28:36',0,NULL,NULL,NULL,NULL,4,1),(27,11,1,'liuliming-apply-monitor-20130117133406','1211','2013-01-17','2013-07-17','2013-01-17 13:34:06',6,2,NULL,NULL,NULL,4,1);

/*Table structure for table `attachment` */

DROP TABLE IF EXISTS `attachment`;

CREATE TABLE `attachment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '附件ID',
  `file_name` varchar(45) NOT NULL COMMENT '文件名',
  `description` varchar(45) DEFAULT NULL COMMENT '文件描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `redmine_issue_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_attachment_redmine_issue1_idx` (`redmine_issue_id`),
  CONSTRAINT `fk_attachment_redmine_issue1` FOREIGN KEY (`redmine_issue_id`) REFERENCES `redmine_issue` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `attachment` */

/*Table structure for table `audit` */

DROP TABLE IF EXISTS `audit`;

CREATE TABLE `audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '审批ID',
  `apply_id` int(11) DEFAULT NULL COMMENT '服务申请ID，服务申请审批时写入',
  `service_tag_id` int(11) DEFAULT NULL COMMENT '服务标签ID，服务变更审批时写入',
  `audit_flow_id` int(11) NOT NULL,
  `create_time` datetime NOT NULL COMMENT '审批时间',
  `result` varchar(1) NOT NULL COMMENT '审批结果：1-同意；2-不同意但继续；3-不同意且退回',
  `opinion` varchar(45) DEFAULT NULL COMMENT '审批意见',
  `status` int(11) NOT NULL COMMENT '状态（为了解决同一个服务标签多次审批无法区分的问题）：1-有效（审批流程未走完）；0-已过期（审批流程已走完）',
  PRIMARY KEY (`id`),
  KEY `fk_audit_apply1_idx` (`apply_id`),
  KEY `fk_audit_audit_flow1_idx` (`audit_flow_id`),
  KEY `fk_audit_service_tag1_idx` (`service_tag_id`),
  CONSTRAINT `fk_audit_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_audit_audit_flow1` FOREIGN KEY (`audit_flow_id`) REFERENCES `audit_flow` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_audit_service_tag1` FOREIGN KEY (`service_tag_id`) REFERENCES `service_tag` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='审批表';

/*Data for the table `audit` */

insert  into `audit`(`id`,`apply_id`,`service_tag_id`,`audit_flow_id`,`create_time`,`result`,`opinion`,`status`) values (1,3,NULL,1,'2013-01-10 10:23:40','1','',1),(2,3,NULL,2,'2013-01-10 10:23:46','1','',1),(3,2,NULL,1,'2013-01-10 10:43:28','1','',1),(4,2,NULL,2,'2013-01-10 10:43:33','1','',1),(5,4,NULL,1,'2013-01-10 10:54:55','1','',1),(6,4,NULL,2,'2013-01-10 10:55:00','1','',1),(7,6,NULL,1,'2013-01-10 11:01:48','1','',1),(8,6,NULL,2,'2013-01-10 11:01:54','1','',1),(9,16,NULL,1,'2013-01-15 14:45:59','1','',1),(10,16,NULL,2,'2013-01-15 14:55:49','1','1212',1),(11,18,NULL,1,'2013-01-15 15:47:58','1','',1),(12,18,NULL,2,'2013-01-15 15:48:07','1','',1),(13,NULL,1,1,'2013-01-15 15:51:52','1','',1),(14,NULL,1,2,'2013-01-15 15:52:03','1','',1),(15,19,NULL,1,'2013-01-15 15:57:01','1','',1),(16,19,NULL,2,'2013-01-15 15:57:08','1','',1),(17,27,NULL,1,'2013-01-17 13:42:23','1','',1),(18,27,NULL,2,'2013-01-17 13:56:05','1','',1),(19,25,NULL,1,'2013-01-17 13:59:19','1','',1),(20,25,NULL,2,'2013-01-17 13:59:26','1','',1);

/*Table structure for table `audit_flow` */

DROP TABLE IF EXISTS `audit_flow`;

CREATE TABLE `audit_flow` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '审批流程ID',
  `flow_type` int(11) NOT NULL COMMENT '流程类型：1-资源申请/变更的审批流程',
  `user_id` int(11) NOT NULL COMMENT '审批人',
  `audit_order` int(11) NOT NULL COMMENT '审批顺序：1-直属领导审批；2...-上级领导审批',
  `is_final` tinyint(1) NOT NULL COMMENT '是否为终审人：1-是；0-否',
  PRIMARY KEY (`id`),
  KEY `fk_audit_flow_user1_idx` (`user_id`),
  CONSTRAINT `fk_audit_flow_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='审批流程表';

/*Data for the table `audit_flow` */

insert  into `audit_flow`(`id`,`flow_type`,`user_id`,`audit_order`,`is_final`) values (1,1,2,2,0),(2,1,3,3,1),(3,1,4,1,0);

/*Table structure for table `bsp_item` */

DROP TABLE IF EXISTS `bsp_item`;

CREATE TABLE `bsp_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `apply_id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_bsp_item_apply1_idx` (`apply_id`),
  KEY `fk_bsp_item_client1_idx` (`client_id`),
  CONSTRAINT `fk_bsp_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_bsp_item_client1` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='BSP服务申请表';

/*Data for the table `bsp_item` */

insert  into `bsp_item`(`id`,`apply_id`,`client_id`) values (1,1,1);

/*Table structure for table `change_item` */

DROP TABLE IF EXISTS `change_item`;

CREATE TABLE `change_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` text NOT NULL COMMENT '详细的变更描述',
  `resources_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_change_item_resources1_idx` (`resources_id`),
  CONSTRAINT `fk_change_item_resources1` FOREIGN KEY (`resources_id`) REFERENCES `resources` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='服务变更明细表';

/*Data for the table `change_item` */

insert  into `change_item`(`id`,`description`,`resources_id`) values (8,'<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;PCS,ECS实例:monitorCompute-ZGM8BW9m<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;监控端口:sdf,df, &#8594 333,3<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;监控进程:ffff,ff, &#8594 33<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;挂载地址: &#8594 null<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;备注:121<br>',1),(15,'<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;PCS,ECS实例:monitorCompute-zbkslsxF<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;监控端口:444, &#8594 444,333<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;监控进程:4, &#8594 11,222<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;挂载地址: &#8594 555,5555<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;备注:12121<br>',4),(16,'<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;PCS,ECS实例:monitorCompute-9BC90WEV<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;监控端口:2, &#8594 2,33,333<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;监控进程:2, &#8594 2,444,555<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;挂载地址:2, &#8594 232323,2323<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;备注:12121<br>',6),(19,'<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;PCS,ECS实例:monitorCompute-C4Z3OfVJ<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;监控端口: &#8594 111<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;监控进程: &#8594 2<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;挂载路径: &#8594 2<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;备注:111<br>',11),(21,'<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;PCS,ECS实例:monitorCompute-87GQP0Qz<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;挂载路径:333, &#8594 333,222<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;备注:1212<br>',12);

/*Table structure for table `client` */

DROP TABLE IF EXISTS `client`;

CREATE TABLE `client` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cp_domain` varchar(45) NOT NULL COMMENT 'CP域名',
  `cp_name` varchar(45) NOT NULL COMMENT '网台名称',
  `contact` varchar(45) DEFAULT NULL COMMENT '联系人',
  `phonenum` varchar(45) DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='MDN服务申请表';

/*Data for the table `client` */

insert  into `client`(`id`,`cp_domain`,`cp_name`,`contact`,`phonenum`) values (1,'1','1','1','1');

/*Table structure for table `compute_item` */

DROP TABLE IF EXISTS `compute_item`;

CREATE TABLE `compute_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) DEFAULT NULL COMMENT '申请ID，可空',
  `identifier` varchar(45) NOT NULL COMMENT '唯一标识，由后台生成，规则：ECS的以ecs开头；PCS的以pcs开头',
  `compute_type` int(11) NOT NULL COMMENT '计算资源类型：1-PCS；2-ECS',
  `os_type` int(11) NOT NULL COMMENT '操作系统类型：1-Windwos2003R2；2-Windwos2008R2；3-Centos5.6；4-Centos6.3',
  `os_bit` int(11) NOT NULL COMMENT '操作系统位数：1-32bit；2-64bit',
  `server_type` int(11) NOT NULL COMMENT '服务器类型：ECS有：1-Small；2-Middle；3-Large；PCS的暂定：4-DELL R410；5-DELL R510；6-DELL R710；7-DELL C6100；8-HP DL2000；9-Aisino 6510；10-SO-5201NR',
  `remark` varchar(45) NOT NULL COMMENT '备注，说明该实例的用途',
  `inner_ip` varchar(45) NOT NULL COMMENT '内网IP，从IP池获取',
  `esg_id` int(11) DEFAULT NULL,
  `elb_id` int(11) DEFAULT NULL,
  `host_name` varchar(45) DEFAULT NULL COMMENT 'PCS、ECS主机名，按命名规则录入，用于存入OneCMDB',
  `server_alias` varchar(45) DEFAULT NULL COMMENT 'PCS所在的物理机别名，来源于OneCMDB',
  `host_server_alias` varchar(45) DEFAULT NULL COMMENT 'ECS所在的宿主机别名，来源于OneCMDB',
  `os_storage_alias` varchar(45) DEFAULT NULL COMMENT 'ECS所在的OS卷别名，来源于OneCMDB',
  `service_tag_id` int(11) DEFAULT NULL COMMENT '服务标签ID，服务变更时写入',
  `old_ip` varchar(45) DEFAULT NULL COMMENT '初始分配的IP，为了解决工单描述不能被更新但运维人又要求显示最新的IP',
  PRIMARY KEY (`id`),
  KEY `fk_compute_item_apply1_idx` (`apply_id`),
  KEY `fk_compute_item_network_esg_item1_idx` (`esg_id`),
  KEY `fk_compute_item_network_elb_item1_idx` (`elb_id`),
  CONSTRAINT `fk_compute_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_compute_item_network_elb_item1` FOREIGN KEY (`elb_id`) REFERENCES `network_elb_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_compute_item_network_esg_item1` FOREIGN KEY (`esg_id`) REFERENCES `network_esg_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='计算资源（虚拟机实例+物理机）明细表';

/*Data for the table `compute_item` */

insert  into `compute_item`(`id`,`apply_id`,`identifier`,`compute_type`,`os_type`,`os_bit`,`server_type`,`remark`,`inner_ip`,`esg_id`,`elb_id`,`host_name`,`server_alias`,`host_server_alias`,`os_storage_alias`,`service_tag_id`,`old_ip`) values (1,1,'ecs-XplJTPhk',2,3,2,2,'数据库','1',1,NULL,'ecs-XplJTPhk',NULL,NULL,NULL,NULL,'0.0.0.0'),(2,1,'ecs-7gy37TCs',2,3,2,2,'数据库','2',1,9,'ecs-7gy37TCs',NULL,NULL,NULL,NULL,'0.0.0.0'),(3,1,'ecs-MF6vLdy0',2,3,2,2,'PortalMinor虚拟机','3',1,1,'ecs-MF6vLdy0',NULL,NULL,NULL,NULL,'0.0.0.0'),(4,1,'ecs-ljDqyALd',2,3,2,2,'业务用途','4',1,9,'ecs-ljDqyALd',NULL,NULL,NULL,NULL,'0.0.0.0'),(5,1,'pcs-3jVkQqDL',1,3,2,5,'PortalMain物理机','5',1,1,'pcs-3jVkQqDL',NULL,NULL,NULL,NULL,'0.0.0.0');

/*Table structure for table `compute_storage_item` */

DROP TABLE IF EXISTS `compute_storage_item`;

CREATE TABLE `compute_storage_item` (
  `compute_item_id` int(11) NOT NULL COMMENT '计算资源ID',
  `storage_item_id` int(11) NOT NULL COMMENT '存储资源ID',
  PRIMARY KEY (`compute_item_id`,`storage_item_id`),
  KEY `fk_compute_storage_item_compute_item1_idx` (`compute_item_id`),
  KEY `fk_compute_storage_item_storage_item1_idx` (`storage_item_id`),
  CONSTRAINT `fk_compute_storage_item_compute_item1` FOREIGN KEY (`compute_item_id`) REFERENCES `compute_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_compute_storage_item_storage_item1` FOREIGN KEY (`storage_item_id`) REFERENCES `storage_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='计算资源与存储资源关联表';

/*Data for the table `compute_storage_item` */

insert  into `compute_storage_item`(`compute_item_id`,`storage_item_id`) values (1,1),(2,1),(3,2),(4,2),(5,2);

/*Table structure for table `department` */

DROP TABLE IF EXISTS `department`;

CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '部门名称',
  `pid` varchar(45) DEFAULT NULL COMMENT '父部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='部门表';

/*Data for the table `department` */

insert  into `department`(`id`,`name`,`pid`) values (1,'新媒体',NULL),(2,'新媒体项目开发部',NULL),(3,'新媒体项目服务部',NULL),(4,'云平台',NULL),(5,'云平台产品部',NULL),(6,'云平台研发部',NULL),(7,'云平台运维部',NULL),(8,'云平台运营部',NULL),(9,'广电业务事业部',NULL),(10,'广电业务事业部系统产品研发部',NULL),(11,'广电业务事业部产品化及支持部',NULL),(12,'MAM产品部',NULL),(13,'业务产品开发部',NULL),(14,'海外事业部',NULL),(15,'海外事业本部基础技术部',NULL),(16,'信息管理办',NULL);

/*Table structure for table `dns_eip_item` */

DROP TABLE IF EXISTS `dns_eip_item`;

CREATE TABLE `dns_eip_item` (
  `dns_item_id` int(11) NOT NULL,
  `eip_item_id` int(11) NOT NULL,
  PRIMARY KEY (`dns_item_id`,`eip_item_id`),
  KEY `fk_dns_eip_item_network_dns_item1_idx` (`dns_item_id`),
  KEY `fk_dns_eip_item_network_eip_item1_idx` (`eip_item_id`),
  CONSTRAINT `fk_dns_eip_item_network_dns_item1` FOREIGN KEY (`dns_item_id`) REFERENCES `network_dns_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_dns_eip_item_network_eip_item1` FOREIGN KEY (`eip_item_id`) REFERENCES `network_eip_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='DNS与EIP关联表';

/*Data for the table `dns_eip_item` */

/*Table structure for table `eip_port_item` */

DROP TABLE IF EXISTS `eip_port_item`;

CREATE TABLE `eip_port_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `eip_id` int(11) NOT NULL COMMENT 'EIP',
  `protocol` varchar(45) NOT NULL COMMENT '协议',
  `eip_port` varchar(45) NOT NULL COMMENT '源端口',
  `instance_port` varchar(45) NOT NULL COMMENT '实例端口',
  PRIMARY KEY (`id`),
  KEY `fk_eip_port_item_network_eip_item1_idx` (`eip_id`),
  CONSTRAINT `fk_eip_port_item_network_eip_item1` FOREIGN KEY (`eip_id`) REFERENCES `network_eip_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='EIP端口映射明细表';

/*Data for the table `eip_port_item` */

insert  into `eip_port_item`(`id`,`eip_id`,`protocol`,`eip_port`,`instance_port`) values (1,1,'HTTP','80','80'),(2,1,'HTTP','8050','8050'),(3,1,'HTTP','8060','8060'),(4,1,'HTTP','8070','8070'),(5,1,'HTTP','8080','8080'),(6,2,'HTTP','80','80');

/*Table structure for table `elb_port_item` */

DROP TABLE IF EXISTS `elb_port_item`;

CREATE TABLE `elb_port_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `elb_id` int(11) NOT NULL,
  `protocol` varchar(45) NOT NULL COMMENT '网络协议',
  `elb_port` varchar(45) NOT NULL COMMENT '负载均衡器端口',
  `instance_port` varchar(45) NOT NULL COMMENT '实例端口',
  PRIMARY KEY (`id`),
  KEY `fk_elb_port_item_network_elb_item1_idx` (`elb_id`),
  CONSTRAINT `fk_elb_port_item_network_elb_item1` FOREIGN KEY (`elb_id`) REFERENCES `network_elb_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='ELB端口映射明细表';

/*Data for the table `elb_port_item` */

insert  into `elb_port_item`(`id`,`elb_id`,`protocol`,`elb_port`,`instance_port`) values (1,1,'HTTP','80','80'),(4,9,'HTTP','80','80');

/*Table structure for table `esg_rule_item` */

DROP TABLE IF EXISTS `esg_rule_item`;

CREATE TABLE `esg_rule_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `esg_id` int(11) NOT NULL COMMENT 'ESG',
  `protocol` varchar(45) NOT NULL COMMENT '绑定规则的协议，如：TCP、UDP、SSH...',
  `port_range` varchar(45) NOT NULL COMMENT '绑定规则的端口范围，如：80，8080-65535',
  `visit_source` varchar(45) NOT NULL COMMENT '绑定规则的访问源，如：192.168.0.1/10，默认：0.0.0.0/0',
  PRIMARY KEY (`id`),
  KEY `fk_esg_rule_item_network_esg_item1_idx` (`esg_id`),
  CONSTRAINT `fk_esg_rule_item_network_esg_item1` FOREIGN KEY (`esg_id`) REFERENCES `network_esg_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='ESG规则明细表';

/*Data for the table `esg_rule_item` */

insert  into `esg_rule_item`(`id`,`esg_id`,`protocol`,`port_range`,`visit_source`) values (1,1,'HTTP','80','0.0.0.0/0');

/*Table structure for table `failure` */

DROP TABLE IF EXISTS `failure`;

CREATE TABLE `failure` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '申报ID',
  `user_id` int(11) NOT NULL COMMENT '申报人ID',
  `title` varchar(45) NOT NULL COMMENT '标题，自动生成规则：用户-故障申报-创建时间（YYYYMMDDHHMMSS）',
  `level` int(11) NOT NULL COMMENT '优先级：1-低；2-普通；3-高；4-紧急；5-立刻',
  `description` varchar(500) NOT NULL COMMENT '故障描述',
  `assignee` int(11) NOT NULL COMMENT '故障受理人ID（从OneCMDB中的Person中读取）',
  `fault_type` int(11) NOT NULL COMMENT '故障类型：1-基础资源；2-MDN；3-VMS；4-云生产',
  `related_id` varchar(100) NOT NULL COMMENT '关联的资源ID集合',
  `create_time` datetime NOT NULL COMMENT '申报时间',
  `redmine_issue_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_fault_user1_idx` (`user_id`),
  KEY `fk_fault_redmine_issue1_idx` (`redmine_issue_id`),
  CONSTRAINT `fk_fault_redmine_issue10` FOREIGN KEY (`redmine_issue_id`) REFERENCES `redmine_issue` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_fault_user10` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='故障申报表';

/*Data for the table `failure` */

insert  into `failure`(`id`,`user_id`,`title`,`level`,`description`,`assignee`,`fault_type`,`related_id`,`create_time`,`redmine_issue_id`) values (1,11,'liuliming-bug-20130110145107',4,'12121',4,1,',2,3,1,','2013-01-10 14:51:07',5),(2,11,'liuliming-bug-20130110145935',4,'2323232',4,1,'1,2,33,','2013-01-10 14:59:35',6),(3,11,'liuliming-bug-20130117141714',4,'1212',4,1,'1','2013-01-17 14:17:14',16);

/*Table structure for table `fault` */

DROP TABLE IF EXISTS `fault`;

CREATE TABLE `fault` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '申报ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `title` varchar(45) NOT NULL COMMENT '标题，自动生成规则：用户-故障申报-创建时间（YYYYMMDDHHMMSS）',
  `level` int(11) NOT NULL COMMENT '优先级：1-低；2-普通；3-高；4-紧急；5-立刻',
  `description` varchar(500) NOT NULL COMMENT '故障描述',
  `assignee` int(11) NOT NULL COMMENT '故障受理人ID（从OneCMDB中的Person中读取）',
  `fault_type` int(11) NOT NULL COMMENT '故障类型：1-单个资源的；2-服务标签级的',
  `related_id` int(11) NOT NULL COMMENT '关联的来源ID：资源ID或服务标签ID',
  `create_time` datetime NOT NULL COMMENT '申报时间',
  `redmine_issue_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_fault_user1_idx` (`user_id`),
  KEY `fk_fault_redmine_issue1_idx` (`redmine_issue_id`),
  CONSTRAINT `fk_fault_redmine_issue1` FOREIGN KEY (`redmine_issue_id`) REFERENCES `redmine_issue` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_fault_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='故障申报表';

/*Data for the table `fault` */

/*Table structure for table `group` */

DROP TABLE IF EXISTS `group`;

CREATE TABLE `group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '组ID',
  `name` varchar(20) NOT NULL COMMENT '角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='角色（组）表';

/*Data for the table `group` */

insert  into `group`(`id`,`name`) values (1,'管理员'),(2,'申请人'),(3,'审批人'),(4,'运维人A-管理员'),(5,'运维人B-操作人');

/*Table structure for table `group_permission` */

DROP TABLE IF EXISTS `group_permission`;

CREATE TABLE `group_permission` (
  `group_id` int(11) NOT NULL COMMENT '角色ID',
  `permission` varchar(45) NOT NULL COMMENT '权限',
  KEY `fk_group_permission_group1_idx` (`group_id`),
  CONSTRAINT `fk_group_permission_group1` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-权限关系表';

/*Data for the table `group_permission` */

insert  into `group_permission`(`group_id`,`permission`) values (2,'apply:view'),(3,'audit:view'),(4,'apply:view'),(4,'operate:view'),(4,'basicData:view'),(4,'summary:view'),(5,'apply:view'),(5,'operate:view'),(5,'basicData:view'),(5,'summary:view'),(1,'user:view'),(1,'group:view'),(1,'apply:view'),(1,'audit:view'),(1,'basicData:view'),(1,'operate:view'),(1,'department:view'),(1,'summary:view');

/*Table structure for table `host_server` */

DROP TABLE IF EXISTS `host_server`;

CREATE TABLE `host_server` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `server_type` int(11) NOT NULL COMMENT '类型：1-宿主机、2-物理机',
  `pool_type` int(11) NOT NULL COMMENT 'IP池',
  `display_name` varchar(45) NOT NULL COMMENT '显示名（来自OneCMDB，组成 ：Company Model Rack-Site）',
  `alias` varchar(45) DEFAULT NULL COMMENT '别名（来自OneCMDB）',
  `location_alias` varchar(45) DEFAULT NULL COMMENT '所在数据中心别名，对应OneCMDB中的Location',
  `ip_address` varchar(45) DEFAULT NULL COMMENT '服务器IP（宿主机只有一个；物理机可能有多个）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='宿主机、物理机表';

/*Data for the table `host_server` */

/*Table structure for table `ip_pool` */

DROP TABLE IF EXISTS `ip_pool`;

CREATE TABLE `ip_pool` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pool_type` int(11) NOT NULL COMMENT 'IP池类型：1-公网IP池-电信；2-公网IP池-联通；3-管理IP池；4-生产IP池；5-公测IP池；6-测试IP池；7-负载均衡虚拟IP池',
  `ip_address` varchar(45) NOT NULL COMMENT 'IP地址',
  `status` int(11) NOT NULL COMMENT '状态：1-未使用；2-已占用（申请保存前，后台定时恢复过期的）；3-已申请（审批完成前）；4-已使用（审批结束）',
  `location` varchar(45) DEFAULT NULL COMMENT '所在数据中心。区域表以后维护',
  `location_alias` varchar(45) DEFAULT NULL COMMENT '所在数据中心别名，对应OneCMDB中的Location',
  `vlan` varchar(45) DEFAULT NULL COMMENT '所属VLAN。VLAN表以后维护',
  `vlan_alias` varchar(45) DEFAULT NULL COMMENT '所属VLAN别名，对应OneCMDB中的Vlans',
  `host_server_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ip_pool_host_server1_idx` (`host_server_id`),
  CONSTRAINT `fk_ip_pool_host_server1` FOREIGN KEY (`host_server_id`) REFERENCES `host_server` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='IP池表';

/*Data for the table `ip_pool` */

insert  into `ip_pool`(`id`,`pool_type`,`ip_address`,`status`,`location`,`location_alias`,`vlan`,`vlan_alias`,`host_server_id`) values (1,0,'0.0.0.0',2,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `location` */

DROP TABLE IF EXISTS `location`;

CREATE TABLE `location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '名字',
  `alias` varchar(45) NOT NULL COMMENT '对应OneCMDB中的Location别名',
  `city` varchar(45) DEFAULT NULL COMMENT '城市',
  `address` varchar(45) DEFAULT NULL COMMENT '地址',
  `postcode` varchar(45) DEFAULT NULL COMMENT '邮编',
  `telephone` varchar(45) DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='IDC表，对应OneCMDB中的Location';

/*Data for the table `location` */

/*Table structure for table `mdn_item` */

DROP TABLE IF EXISTS `mdn_item`;

CREATE TABLE `mdn_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `apply_id` int(11) DEFAULT NULL COMMENT '服务申请ID，服务申请时写入',
  `client_id` int(11) DEFAULT NULL,
  `bsp_id` int(11) DEFAULT NULL COMMENT '当作为BSP的一项子服务时，值为其所属BSP的ID，另外两项apply_id和client_id从BSP中拷贝',
  `identifier` varchar(45) NOT NULL COMMENT '标识符：mdn-',
  `page_domain` varchar(45) NOT NULL COMMENT '页面加速域名',
  `page_ip` varchar(45) DEFAULT NULL COMMENT '页面加速IP',
  `image_domain` varchar(45) NOT NULL COMMENT '图片加速域名',
  `image_ip` varchar(45) DEFAULT NULL COMMENT '图片加速IP',
  `live_guid` varchar(64) NOT NULL COMMENT '直播加速流GUID',
  `vod_domain` varchar(45) NOT NULL COMMENT '点播域名',
  `vod_streamer` varchar(45) NOT NULL COMMENT '点播源站Streamer地址',
  `service_tag_id` int(11) DEFAULT NULL COMMENT '服务标签ID，服务变更时写入',
  PRIMARY KEY (`id`),
  KEY `fk_mdn_item_apply1_idx` (`apply_id`),
  KEY `fk_mdn_item_bsp_item1_idx` (`bsp_id`),
  KEY `fk_mdn_item_client1_idx` (`client_id`),
  CONSTRAINT `fk_mdn_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_mdn_item_bsp_item1` FOREIGN KEY (`bsp_id`) REFERENCES `bsp_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_mdn_item_client1` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='MDN服务申请表';

/*Data for the table `mdn_item` */

/*Table structure for table `mdn_live_item` */

DROP TABLE IF EXISTS `mdn_live_item`;

CREATE TABLE `mdn_live_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `mdn_id` int(11) NOT NULL,
  `type` varchar(45) NOT NULL COMMENT '流类型：FLV_H264_120K_FLV、M3U8_H264_800K_TS、RTMP_H264_800K_FLV、RTSP_H264_120K_3GP',
  `url` varchar(45) NOT NULL COMMENT '编码器输出Or目标地址',
  PRIMARY KEY (`id`),
  KEY `fk_mdn_live_item_mdn_item1_idx` (`mdn_id`),
  CONSTRAINT `fk_mdn_live_item_mdn_item1` FOREIGN KEY (`mdn_id`) REFERENCES `mdn_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='直播加速流及地址表';

/*Data for the table `mdn_live_item` */

/*Table structure for table `mdn_node_item` */

DROP TABLE IF EXISTS `mdn_node_item`;

CREATE TABLE `mdn_node_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `mdn_id` int(11) NOT NULL,
  `node_id` varchar(45) NOT NULL COMMENT '节点ID',
  `node_name` varchar(45) NOT NULL COMMENT '节点名称',
  PRIMARY KEY (`id`),
  KEY `fk_mdn_node_item_mdn_item1_idx` (`mdn_id`),
  CONSTRAINT `fk_mdn_node_item_mdn_item1` FOREIGN KEY (`mdn_id`) REFERENCES `mdn_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='MDN分发节点表';

/*Data for the table `mdn_node_item` */

/*Table structure for table `monitor_compute` */

DROP TABLE IF EXISTS `monitor_compute`;

CREATE TABLE `monitor_compute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apply_id` int(11) DEFAULT NULL,
  `identifier` varchar(45) NOT NULL COMMENT '标识符',
  `ip_address` varchar(45) NOT NULL COMMENT '服务器IP',
  `cpu_warn` varchar(5) DEFAULT NULL COMMENT 'CPU占用率-报警阀值',
  `cpu_critical` varchar(5) DEFAULT NULL COMMENT 'CPU占用率-严重警告阀值',
  `memory_warn` varchar(5) DEFAULT NULL COMMENT '内存占用率-报警阀值',
  `memory_critical` varchar(5) DEFAULT NULL COMMENT '内存占用率-严重警告阀值',
  `disk_warn` varchar(5) DEFAULT NULL COMMENT '硬盘可用率-报警阀值',
  `disk_critical` varchar(5) DEFAULT NULL COMMENT '硬盘可用率-严重警告阀值',
  `ping_loss_warn` varchar(5) DEFAULT NULL COMMENT '网络丢包率-报警阀值',
  `ping_loss_critical` varchar(5) DEFAULT NULL COMMENT '网络丢包率-严重警告阀值',
  `ping_delay_warn` varchar(10) DEFAULT NULL COMMENT '网络延时-报警阀值',
  `ping_delay_critical` varchar(10) DEFAULT NULL COMMENT '网络延时-严重警告阀值',
  `max_process_warn` varchar(5) DEFAULT NULL COMMENT '最大进程数-报警阀值',
  `max_process_critical` varchar(5) DEFAULT NULL COMMENT '最大进程数-严重警告阀值',
  `port` varchar(200) DEFAULT NULL COMMENT '监控端口，以“,”隔开',
  `process` varchar(200) DEFAULT NULL COMMENT '监控进程，以“,”隔开',
  `mount_point` varchar(200) DEFAULT NULL COMMENT '挂载路径，以“,”隔开',
  PRIMARY KEY (`id`),
  KEY `fk_monitor_compute_apply1_idx` (`apply_id`),
  CONSTRAINT `fk_monitor_compute_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COMMENT='监控服务计算资源参数表';

/*Data for the table `monitor_compute` */

insert  into `monitor_compute`(`id`,`apply_id`,`identifier`,`ip_address`,`cpu_warn`,`cpu_critical`,`memory_warn`,`memory_critical`,`disk_warn`,`disk_critical`,`ping_loss_warn`,`ping_loss_critical`,`ping_delay_warn`,`ping_delay_critical`,`max_process_warn`,`max_process_critical`,`port`,`process`,`mount_point`) values (14,NULL,'monitorCompute-ZGM8BW9m','0.0.0.0','1','1','1','1','1','1','1','1','1','1','5','5','sdf,df,,','ffff,ff,',NULL),(15,NULL,'monitorCompute-ZGM8BW9m','0.0.0.0','1','1','1','1','1','1','1','1','1','1','5','5','sdf,df,11,22','ffff,ff,',NULL),(16,NULL,'monitorCompute-ZGM8BW9m','0.0.0.0','1','1','1','1','1','1','1','1','1','1','5','5','333,3','33',NULL),(17,NULL,'monitorCompute-zbkslsxF','0.0.0.0','1','1','1','1',NULL,NULL,'1','1',NULL,NULL,NULL,NULL,'444,333','11,222','555,5555'),(23,16,'monitorCompute-9bGE4Pgf','0.0.0.0','1','1','1','1','1','1','1','1','1','1','11','11','1,2222','2,333','2,4444'),(24,16,'monitorCompute-9BC90WEV','0.0.0.0','1','1','1','1',NULL,NULL,'1','1',NULL,NULL,NULL,NULL,'2,','2,','2,'),(25,NULL,'monitorCompute-9BC90WEV','0.0.0.0','1','1','1','1',NULL,NULL,'1','1',NULL,NULL,NULL,NULL,'2,33,333','2,444,555','232323,2323'),(27,18,'monitorCompute-0vZayUXZ','0.0.0.0','1','1','1','1','1','1','1','1','1','1',NULL,NULL,'2,','2,','2,'),(28,18,'monitorCompute-7Ang56jF','0.0.0.0','1','1','1','1','1','1','1','1','1','1',NULL,NULL,'2,','2,','2,'),(29,NULL,'monitorCompute-7Ang56jF','0.0.0.0','1','1','1','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2,33333','2,121是','2,sdfsdfsdf'),(30,19,'monitorCompute-C4Z3OfVJ','0.0.0.0','1','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','',''),(31,NULL,'monitorCompute-C4Z3OfVJ','0.0.0.0','1','1',NULL,NULL,NULL,NULL,NULL,NULL,'1','1',NULL,NULL,'111','2','2'),(32,20,'monitorCompute-CtXnm7WS','0.0.0.0','1','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','',''),(33,20,'monitorCompute-rqG6fjCn','0.0.0.0','1','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','',''),(34,21,'monitorCompute-8nkHby3B','0.0.0.0','1','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','',''),(35,22,'monitorCompute-GEpvIPXJ','0.0.0.0','1','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','',''),(36,23,'monitorCompute-qu6dAl40','0.0.0.0','1','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','',''),(37,24,'monitorCompute-itS10fF4','1',NULL,NULL,'1','1','1','1','1','1','1','1',NULL,NULL,',','1',''),(38,27,'monitorCompute-87GQP0Qz','1','1','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'11','222','333,'),(39,NULL,'monitorCompute-87GQP0Qz','1','1','1',NULL,NULL,NULL,NULL,NULL,NULL,'1','1',NULL,NULL,'11','222','333,222');

/*Table structure for table `monitor_elb` */

DROP TABLE IF EXISTS `monitor_elb`;

CREATE TABLE `monitor_elb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apply_id` int(11) DEFAULT NULL,
  `elb_id` int(11) NOT NULL,
  `identifier` varchar(45) NOT NULL COMMENT '标识符',
  PRIMARY KEY (`id`),
  KEY `fk_monitor_storage_apply1_idx` (`apply_id`),
  KEY `fk_monitor_elb_network_elb_item1_idx` (`elb_id`),
  CONSTRAINT `fk_monitor_elb_network_elb_item1` FOREIGN KEY (`elb_id`) REFERENCES `network_elb_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_monitor_storage_apply10` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='监控服务ELB资源参数表';

/*Data for the table `monitor_elb` */

insert  into `monitor_elb`(`id`,`apply_id`,`elb_id`,`identifier`) values (11,16,1,'monitorElb-tN6H0m78'),(12,26,9,'monitorElb-1EDZjhHE');

/*Table structure for table `monitor_mail` */

DROP TABLE IF EXISTS `monitor_mail`;

CREATE TABLE `monitor_mail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) NOT NULL COMMENT '邮件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='监控服务邮件表';

/*Data for the table `monitor_mail` */

insert  into `monitor_mail`(`id`,`email`) values (1,'email1'),(2,'email2'),(3,'email1'),(4,'email2'),(5,'email1'),(6,'email11'),(7,'22'),(8,'1212'),(9,'1');

/*Table structure for table `monitor_mail_apply` */

DROP TABLE IF EXISTS `monitor_mail_apply`;

CREATE TABLE `monitor_mail_apply` (
  `apply_id` int(11) NOT NULL,
  `monitor_mail_id` int(11) NOT NULL,
  PRIMARY KEY (`apply_id`,`monitor_mail_id`),
  KEY `fk_monitor_mail_apply_monitor_mail1_idx` (`monitor_mail_id`),
  CONSTRAINT `fk_monitor_mail_apply_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_monitor_mail_apply_monitor_mail1` FOREIGN KEY (`monitor_mail_id`) REFERENCES `monitor_mail` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='监控服务邮件与申请关联表';

/*Data for the table `monitor_mail_apply` */

insert  into `monitor_mail_apply`(`apply_id`,`monitor_mail_id`) values (16,3),(16,4),(18,5),(18,6),(19,7),(22,8),(24,9);

/*Table structure for table `monitor_phone` */

DROP TABLE IF EXISTS `monitor_phone`;

CREATE TABLE `monitor_phone` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `telephone` varchar(45) NOT NULL COMMENT '手机号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='监控服务电话表';

/*Data for the table `monitor_phone` */

insert  into `monitor_phone`(`id`,`telephone`) values (1,'phone1'),(2,'phone2'),(3,'phone1'),(4,'phone2'),(5,'phone1'),(6,'phone2'),(7,'2222'),(8,'12'),(9,'2');

/*Table structure for table `monitor_phone_apply` */

DROP TABLE IF EXISTS `monitor_phone_apply`;

CREATE TABLE `monitor_phone_apply` (
  `apply_id` int(11) NOT NULL,
  `monitor_phone_id` int(11) NOT NULL,
  PRIMARY KEY (`apply_id`,`monitor_phone_id`),
  KEY `fk_monitor_phone_apply_monitor_phone1_idx` (`monitor_phone_id`),
  CONSTRAINT `fk_monitor_phone_apply_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_monitor_phone_apply_monitor_phone1` FOREIGN KEY (`monitor_phone_id`) REFERENCES `monitor_phone` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='监控服务电话与申请关联表';

/*Data for the table `monitor_phone_apply` */

insert  into `monitor_phone_apply`(`apply_id`,`monitor_phone_id`) values (16,3),(16,4),(18,5),(18,6),(19,7),(22,8),(24,9);

/*Table structure for table `network_dns_item` */

DROP TABLE IF EXISTS `network_dns_item`;

CREATE TABLE `network_dns_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `apply_id` int(11) DEFAULT NULL COMMENT '申请ID，可空',
  `identifier` varchar(45) NOT NULL COMMENT '标识符',
  `domain_name` varchar(45) NOT NULL COMMENT '域名',
  `domain_type` int(11) NOT NULL COMMENT '域名类型：1-GSLB；2-A；3-CNAME',
  `cname_domain` varchar(45) DEFAULT NULL COMMENT 'CNAME域名，若域名类型为CNAME，则需填写此字段。',
  `service_tag_id` int(11) DEFAULT NULL COMMENT '服务标签ID，服务变更时写入',
  PRIMARY KEY (`id`),
  KEY `fk_network_dns_item_apply1_idx` (`apply_id`),
  CONSTRAINT `fk_network_dns_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网络资源DNS明细表';

/*Data for the table `network_dns_item` */

/*Table structure for table `network_eip_item` */

DROP TABLE IF EXISTS `network_eip_item`;

CREATE TABLE `network_eip_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) DEFAULT NULL COMMENT '服务申请ID，服务申请时写入',
  `identifier` varchar(45) NOT NULL COMMENT '标识符',
  `isp_type` int(11) NOT NULL COMMENT '运营商类型：1-电信；2-联通；3-移动',
  `ip_address` varchar(45) NOT NULL COMMENT '联通公网IP',
  `elb_id` int(11) DEFAULT NULL COMMENT '关联ELB',
  `compute_id` int(11) DEFAULT NULL COMMENT '关联计算资源',
  `service_tag_id` varchar(45) DEFAULT NULL COMMENT '服务标签ID，服务变更时写入',
  `old_ip` varchar(45) DEFAULT NULL COMMENT '初始分配的IP',
  PRIMARY KEY (`id`),
  KEY `fk_network_eip_item_apply1_idx` (`apply_id`),
  KEY `fk_network_eip_item_network_elb_item1_idx` (`elb_id`),
  KEY `fk_network_eip_item_compute_item1_idx` (`compute_id`),
  CONSTRAINT `fk_network_eip_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_network_eip_item_compute_item1` FOREIGN KEY (`compute_id`) REFERENCES `compute_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_network_eip_item_network_elb_item1` FOREIGN KEY (`elb_id`) REFERENCES `network_elb_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='网络资源EIP明细表';

/*Data for the table `network_eip_item` */

insert  into `network_eip_item`(`id`,`apply_id`,`identifier`,`isp_type`,`ip_address`,`elb_id`,`compute_id`,`service_tag_id`,`old_ip`) values (1,1,'eip-pxnbt42p',1,'0.0.0.0',NULL,4,NULL,'0.0.0.0'),(2,1,'eip-2S7uNC1c',1,'0.0.0.0',1,NULL,NULL,'0.0.0.0');

/*Table structure for table `network_elb_item` */

DROP TABLE IF EXISTS `network_elb_item`;

CREATE TABLE `network_elb_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) DEFAULT NULL COMMENT '服务申请ID，服务申请时写入',
  `identifier` varchar(45) NOT NULL COMMENT '标识符',
  `keep_session` tinyint(1) NOT NULL COMMENT '是否保持会话：1-是；0-否',
  `virtual_ip` varchar(45) NOT NULL COMMENT '负载均衡虚拟IP，从IP池获取',
  `bandwith` int(11) DEFAULT NULL COMMENT '带宽，单位：M',
  `service_tag_id` int(11) DEFAULT NULL COMMENT '服务标签ID，服务变更时写入',
  PRIMARY KEY (`id`),
  KEY `fk_in_vpn_item_apply1_idx` (`apply_id`),
  CONSTRAINT `fk_in_vpn_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='网络资源ELB明细表';

/*Data for the table `network_elb_item` */

insert  into `network_elb_item`(`id`,`apply_id`,`identifier`,`keep_session`,`virtual_ip`,`bandwith`,`service_tag_id`) values (1,1,'elb-1Swa0Gow',0,'0.0.0.0',2,NULL),(9,25,'elb-XHYPXC3m',0,'0.0.0.0',NULL,0);

/*Table structure for table `network_esg_item` */

DROP TABLE IF EXISTS `network_esg_item`;

CREATE TABLE `network_esg_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `identifier` varchar(45) NOT NULL COMMENT '标识符',
  `description` varchar(45) NOT NULL COMMENT '安全组的描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='网络资源ESG明细表';

/*Data for the table `network_esg_item` */

insert  into `network_esg_item`(`id`,`user_id`,`identifier`,`description`) values (1,11,'esg-OXyZBe1P','BSP标准模板ESG');

/*Table structure for table `redmine_issue` */

DROP TABLE IF EXISTS `redmine_issue`;

CREATE TABLE `redmine_issue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue_id` int(11) DEFAULT NULL COMMENT 'Redmine中的Issue主键',
  `tracker_id` int(11) NOT NULL COMMENT 'Redmine中的Tracker',
  `project_id` int(11) NOT NULL COMMENT 'Redmine中的Project',
  `subject` varchar(255) NOT NULL COMMENT 'Redmine中的subject',
  `assignee` int(11) NOT NULL COMMENT '下一个操作人，默认首先指派给余波',
  `status` int(11) NOT NULL COMMENT '状态，通Redmine中的状态：1-新建；2-处理中；5-已关闭',
  `resource_id` varchar(255) DEFAULT NULL COMMENT '该工单关联的资源ID（用于下线回收操作），多个ID以\\",\\"隔开',
  `apply_id` int(11) DEFAULT NULL COMMENT '服务申请ID，服务申请时写入',
  `service_tag_id` int(11) DEFAULT NULL COMMENT '服务标签ID，服务变更时写入',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='工单处理表（同步Redmine中的Issue表）';

/*Data for the table `redmine_issue` */

insert  into `redmine_issue`(`id`,`issue_id`,`tracker_id`,`project_id`,`subject`,`assignee`,`status`,`resource_id`,`apply_id`,`service_tag_id`) values (1,432,3,1,'liuliming-apply-monitor-20130109170406',4,1,NULL,3,NULL),(2,434,3,1,'liuliming-apply-monitor-20130109170349',4,5,NULL,2,NULL),(3,435,3,1,'liuliming-apply-monitor-20130110105416',4,5,NULL,4,NULL),(4,436,3,1,'liuliming-apply-monitor-20130110110125',4,5,NULL,6,NULL),(5,438,1,3,'liuliming-bug-20130110145107',4,1,NULL,NULL,NULL),(6,439,1,3,'liuliming-bug-20130110145935',4,1,NULL,NULL,NULL),(7,441,2,1,'liuliming-recycle-20130114112049',4,5,'2,',NULL,NULL),(8,442,3,1,'liuliming-apply-monitor-20130115140741',4,5,NULL,16,NULL),(9,443,3,1,'liuliming-apply-monitor-20130115154621',4,5,NULL,18,NULL),(10,444,2,1,'liuliming-change-20130115155203',4,5,NULL,NULL,1),(11,445,2,1,'liuliming-recycle-20130115155520',4,5,'10,',NULL,NULL),(12,446,3,1,'liuliming-apply-monitor-20130115155634',4,5,NULL,19,NULL),(13,450,2,1,'liuliming-recycle-20130116095631',4,5,'8,',NULL,NULL),(14,452,3,1,'liuliming-apply-monitor-20130117133406',4,5,NULL,27,NULL),(15,453,3,1,'liuliming-apply-20130117112700',4,5,NULL,25,NULL),(16,454,1,3,'liuliming-bug-20130117141714',4,1,NULL,NULL,NULL);

/*Table structure for table `resources` */

DROP TABLE IF EXISTS `resources`;

CREATE TABLE `resources` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '所属申请人',
  `service_type` int(11) NOT NULL COMMENT '各个单项的服务类型：1-PCS、2-ECS、3-ES3、4-ELB、5-EIP、6-DNS、7-ESG',
  `service_tag_id` int(11) DEFAULT NULL COMMENT '自定义服务标签',
  `old_service_id` int(11) NOT NULL COMMENT '原各单项服务的主键ID，如果再次进行变更，则为前次的new_service_id',
  `service_identifier` varchar(45) NOT NULL COMMENT '原各单项服务的标识符',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `status` int(11) NOT NULL COMMENT '状态：-1-未变更（目前是走完审批流程，以后可能是创建完成才能变更）；0-已变更（未提交）；1-待审批（已提交）；2-审批中；3-已退回；4-已审批；5-创建中；6-已创建；7-回收中',
  `new_service_id` int(11) DEFAULT NULL COMMENT '状态：0-已申请；1-待审核；2-审核中；3-已退回；4-已审核；5-创建中；6-已创建',
  `update_time` datetime DEFAULT NULL COMMENT '变更时间',
  `usedby` int(11) DEFAULT NULL COMMENT '运维人ID',
  `note` varchar(500) DEFAULT NULL COMMENT '备注',
  `ip_address` varchar(45) DEFAULT NULL COMMENT 'IP地址',
  PRIMARY KEY (`id`),
  KEY `fk_resources_service_tag1_idx` (`service_tag_id`),
  CONSTRAINT `fk_resources_service_tag1` FOREIGN KEY (`service_tag_id`) REFERENCES `service_tag` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='资源总表（服务申请时向此表写入记录，服务变更时更新此记录的新申请ID和新服务ID）';

/*Data for the table `resources` */

insert  into `resources`(`id`,`user_id`,`service_type`,`service_tag_id`,`old_service_id`,`service_identifier`,`create_time`,`status`,`new_service_id`,`update_time`,`usedby`,`note`,`ip_address`) values (1,11,9,1,7,'monitorCompute-ZGM8BW9m','2013-01-10 10:55:14',6,16,NULL,NULL,'121','0.0.0.0'),(3,11,9,NULL,10,'monitorCompute-bwamk0eF','2013-01-10 11:02:11',-1,NULL,NULL,NULL,NULL,'0.0.0.0'),(4,11,9,1,11,'monitorCompute-zbkslsxF','2013-01-10 11:02:11',6,17,NULL,NULL,'12121','0.0.0.0'),(5,11,10,1,4,'monitorElb-nVQtHbL7','2013-01-10 11:02:11',-1,NULL,NULL,NULL,'1212','0.0.0.0'),(6,11,9,1,24,'monitorCompute-9BC90WEV','2013-01-15 14:56:27',6,25,NULL,NULL,'12121','0.0.0.0'),(7,11,9,NULL,23,'monitorCompute-9bGE4Pgf','2013-01-15 14:56:27',-1,NULL,NULL,NULL,NULL,'0.0.0.0'),(9,11,9,NULL,27,'monitorCompute-0vZayUXZ','2013-01-15 15:49:27',-1,NULL,NULL,NULL,NULL,'0.0.0.0'),(11,11,9,1,30,'monitorCompute-C4Z3OfVJ','2013-01-15 15:57:20',0,31,NULL,NULL,'111','0.0.0.0'),(12,11,9,1,38,'monitorCompute-87GQP0Qz','2013-01-17 13:56:30',0,39,NULL,11,'1212','1'),(13,11,4,NULL,9,'elb-XHYPXC3m','2013-01-17 13:59:39',-1,NULL,NULL,NULL,NULL,'0.0.0.0');

/*Table structure for table `service_tag` */

DROP TABLE IF EXISTS `service_tag`;

CREATE TABLE `service_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '服务标签名称（或网台名称）',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `service_type` int(11) NOT NULL COMMENT '服务类型：1-MDN、2-BSP、3-自定义服务',
  `resource_type` int(11) NOT NULL COMMENT '资源类型，同apply表',
  `service_start` varchar(10) NOT NULL COMMENT '服务起始时间，同apply表',
  `service_end` varchar(10) NOT NULL COMMENT '服务截止时间，同apply表',
  `description` varchar(500) NOT NULL COMMENT '描述用途等',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `status` int(11) NOT NULL COMMENT '状态：-1-未变更；0-已变更（未提交）；1-待审批（已提交）；2-审批中；3-已退回；4-已审批',
  `audit_flow_id` int(11) DEFAULT NULL COMMENT '当前所在审批流程ID',
  `domain` varchar(45) DEFAULT NULL,
  `contact` varchar(45) DEFAULT NULL,
  `phonenum` varchar(45) DEFAULT NULL,
  `redmine_issue_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_service_tag_audit_flow1_idx` (`audit_flow_id`),
  CONSTRAINT `fk_service_tag_audit_flow1` FOREIGN KEY (`audit_flow_id`) REFERENCES `audit_flow` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='自定义服务类型标签表（用于用户自己关联其下所有资源）';

/*Data for the table `service_tag` */

insert  into `service_tag`(`id`,`name`,`user_id`,`service_type`,`resource_type`,`service_start`,`service_end`,`description`,`create_time`,`status`,`audit_flow_id`,`domain`,`contact`,`phonenum`,`redmine_issue_id`) values (1,'121',11,3,3,'2013-01-11','2013-07-11','11','2013-01-11 16:54:18',0,2,'1','1','1',NULL);

/*Table structure for table `storage_item` */

DROP TABLE IF EXISTS `storage_item`;

CREATE TABLE `storage_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) DEFAULT NULL COMMENT '申请ID，可空',
  `identifier` varchar(45) NOT NULL COMMENT '唯一标识，由后台生成，规则：？',
  `space` int(11) NOT NULL COMMENT '存储空间：以GB为单位，如：20GB',
  `storage_type` int(11) NOT NULL COMMENT '存储类型：1-Fimas(高吞吐量)；2-Netapp(高IOPS)',
  `controller_alias` varchar(45) DEFAULT NULL COMMENT 'OneCMDB中的Fimas、Netapp控制器别名',
  `volume` varchar(45) DEFAULT NULL COMMENT '挂载卷/盘符',
  `service_tag_id` int(11) DEFAULT NULL COMMENT '服务标签ID，服务变更时写入',
  `mount_point` varchar(45) DEFAULT NULL COMMENT '挂载点',
  PRIMARY KEY (`id`),
  KEY `fk_storage_item_apply1_idx` (`apply_id`),
  CONSTRAINT `fk_storage_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='存储资源明细表';

/*Data for the table `storage_item` */

insert  into `storage_item`(`id`,`apply_id`,`identifier`,`space`,`storage_type`,`controller_alias`,`volume`,`service_tag_id`,`mount_point`) values (1,1,'es3-1x7QQsgX',20,2,NULL,NULL,NULL,NULL),(2,1,'es3-Othh7Zwc',200,1,NULL,NULL,NULL,NULL);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `name` varchar(15) NOT NULL COMMENT '用户姓名',
  `password` varchar(45) NOT NULL COMMENT '用户密码',
  `email` varchar(45) NOT NULL COMMENT 'Sobey邮箱（登录名）',
  `phonenum` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `department_id` int(11) DEFAULT NULL COMMENT '所属部门ID',
  `leader_id` int(11) DEFAULT NULL COMMENT '直属领导ID',
  `type` int(11) DEFAULT NULL COMMENT '用户类型：1-管理员；2-申请人；3-审核人；4-运维人',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态：1-正常；2-无效',
  `redmine_user_id` int(11) DEFAULT NULL COMMENT 'Redmine中的用户ID，运维人员才有。',
  `login_name` varchar(45) DEFAULT NULL,
  `salt` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_department1_idx` (`department_id`),
  CONSTRAINT `fk_user_department1` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`password`,`email`,`phonenum`,`department_id`,`leader_id`,`type`,`create_time`,`login_time`,`status`,`redmine_user_id`,`login_name`,`salt`) values (1,'admin','admin','admin','1',1,NULL,1,'2012-07-01 12:00:00',NULL,1,NULL,NULL,NULL),(2,'张鹏','zhangpeng01','zhangpeng01','1',7,3,3,'2012-07-01 12:00:00',NULL,1,NULL,NULL,NULL),(3,'陈路','ca191409e46486e5a328f85c4206d1cb','chenlu','1',1,NULL,3,'2012-07-01 12:00:00',NULL,1,NULL,NULL,NULL),(4,'张汨','zhangmi','zhangmi','1',6,2,3,'2012-07-01 12:00:00',NULL,1,NULL,NULL,NULL),(6,'余波','ca191409e46486e5a328f85c4206d1cb','yubo01','1',7,2,4,'2012-08-11 12:00:00',NULL,1,4,NULL,NULL),(7,'艾磊','ailei','ailei','1',7,2,5,'2012-08-11 12:00:00',NULL,1,5,NULL,NULL),(8,'杨飞','yangfei','yangfei','1',7,2,5,'2012-08-11 12:00:00',NULL,1,6,NULL,NULL),(9,'胡光俊','huguangjun','huguangjun','1',7,2,5,'2012-08-11 12:00:00',NULL,1,7,NULL,NULL),(10,'李乾星','ca191409e46486e5a328f85c4206d1cb','liqianxing','1',7,2,4,'2012-08-11 12:00:00',NULL,1,8,NULL,NULL),(11,'刘力铭','ca191409e46486e5a328f85c4206d1cb','liuliming','1',7,2,4,'2012-08-11 12:00:00',NULL,1,9,NULL,NULL),(12,'苏颖','suying','suying','1',7,2,5,'2012-08-11 12:00:00',NULL,1,10,NULL,NULL),(13,'温路平','wenlp','wenlp','1',6,4,2,'2012-08-11 12:00:00',NULL,1,NULL,NULL,NULL),(14,'h98gjuguyg8','ffb8fe372bd14c80ce483bcafca0c710271ad008','kai8406@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,'1111','59c6ceeeab2115fb');

/*Table structure for table `user_group` */

DROP TABLE IF EXISTS `user_group`;

CREATE TABLE `user_group` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `group_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`group_id`),
  KEY `fk_user_group_group1_idx` (`group_id`),
  KEY `fk_user_group_user1_idx` (`user_id`),
  CONSTRAINT `fk_user_group_group1` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_group_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-角色关系表';

/*Data for the table `user_group` */

insert  into `user_group`(`user_id`,`group_id`) values (1,1),(14,1),(13,2),(14,2),(2,3),(3,3),(4,3),(6,4),(10,4),(11,4),(7,5),(8,5),(9,5),(12,5);

/*Table structure for table `vlan` */

DROP TABLE IF EXISTS `vlan`;

CREATE TABLE `vlan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '名字',
  `description` varchar(45) NOT NULL COMMENT '描述',
  `alias` varchar(45) NOT NULL COMMENT '对应OneCMDB中的Vlans别名',
  `location_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_vlan_location1_idx` (`location_id`),
  CONSTRAINT `fk_vlan_location1` FOREIGN KEY (`location_id`) REFERENCES `location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='VLAN表，对应OneCMDB中的Vlans';

/*Data for the table `vlan` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
