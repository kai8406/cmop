/*
SQLyog Trial v10.51 
MySQL - 5.5.25 : Database - cmop
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
  `title` varchar(45) NOT NULL COMMENT '标题，自动生成规则：用户-服务类型-创建时间（YYYYMMDDHHMMSS）',
  `service_tag` varchar(45) NOT NULL COMMENT '服务标签',
  `service_type` int(11) NOT NULL COMMENT '大服务类型：1-基础设施；2-MDN；3-云生产；4-监控',
  `priority` int(11) NOT NULL COMMENT '优先级：2-普通；3-高；4-紧急',
  `description` varchar(500) NOT NULL COMMENT '用途描述',
  `service_start` varchar(10) NOT NULL COMMENT '服务开始时间',
  `service_end` varchar(10) NOT NULL COMMENT '服务结束时间',
  `create_time` datetime NOT NULL COMMENT '申请/变更时间',
  `status` int(11) NOT NULL COMMENT '状态：0-已申请；1-待审核；2-审核中；3-已退回；4-已审核；5-处理中；6-已创建',
  `audit_flow_id` int(11) DEFAULT NULL COMMENT '当前所在审批流程ID',
  `redmine_issue_id` int(11) DEFAULT NULL COMMENT '关联Redmine的ID',
  PRIMARY KEY (`id`),
  KEY `fk_apply_user1_idx` (`user_id`),
  KEY `fk_apply_redmine_issue1_idx` (`redmine_issue_id`),
  KEY `fk_apply_audit_flow1_idx` (`audit_flow_id`),
  CONSTRAINT `fk_apply_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_apply_redmine_issue1` FOREIGN KEY (`redmine_issue_id`) REFERENCES `redmine_issue` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_apply_audit_flow1` FOREIGN KEY (`audit_flow_id`) REFERENCES `audit_flow` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源申请表（也同样适合于服务变更，变更时先向此表写入一行新的记录，再向各个单项写入新的记录）';

/*Data for the table `apply` */

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='审批表';

/*Data for the table `audit` */

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

/*Table structure for table `change` */

DROP TABLE IF EXISTS `change`;

CREATE TABLE `change` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resources_id` int(11) NOT NULL COMMENT '资源ID',
  `user_id` int(11) NOT NULL COMMENT '变更人',
  `change_time` datetime NOT NULL COMMENT '变更时间',
  `description` varchar(200) DEFAULT NULL COMMENT '变更描述',
  PRIMARY KEY (`id`),
  KEY `fk_change_item_resources1_idx` (`resources_id`),
  CONSTRAINT `fk_change_item_resources1` FOREIGN KEY (`resources_id`) REFERENCES `resources` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务变更表';

/*Data for the table `change` */

/*Table structure for table `change_item` */

DROP TABLE IF EXISTS `change_item`;

CREATE TABLE `change_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `change_id` int(11) NOT NULL,
  `field_name` varchar(45) NOT NULL COMMENT '变更项（字段）名称',
  `old_value` varchar(200) NOT NULL COMMENT '原值',
  `new_value` varchar(200) NOT NULL COMMENT '新值',
  PRIMARY KEY (`id`),
  KEY `fk_change_item_change1_idx` (`change_id`),
  CONSTRAINT `fk_change_item_change1` FOREIGN KEY (`change_id`) REFERENCES `change` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='变更明细表';

/*Data for the table `change_item` */

/*Table structure for table `compute_item` */

DROP TABLE IF EXISTS `compute_item`;

CREATE TABLE `compute_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `identifier` varchar(45) NOT NULL COMMENT '唯一标识，由后台生成，规则：ECS的以ecs开头；PCS的以pcs开头',
  `compute_type` int(11) NOT NULL COMMENT '计算资源类型：1-PCS；2-ECS',
  `os_type` int(11) NOT NULL COMMENT '操作系统类型：1-Windwos2003R2；2-Windwos2008R2；3-Centos5.6；4-Centos6.3',
  `os_bit` int(11) NOT NULL COMMENT '操作系统位数：1-32bit；2-64bit',
  `server_type` int(11) NOT NULL COMMENT '服务器类型：ECS有：1-Small；2-Middle；3-Large；PCS的暂定：4-DELL R410；5-DELL R510；6-DELL R710；7-DELL C6100；8-HP DL2000；9-Aisino 6510；10-SO-5201NR',
  `remark` varchar(45) NOT NULL COMMENT '备注，说明该实例的用途',
  `inner_ip` varchar(45) NOT NULL COMMENT '内网IP，从IP池获取',
  `old_ip` varchar(45) NOT NULL COMMENT '初始分配的IP，为了解决工单描述不能被更新但运维人又要求显示最新的IP',
  `esg_id` int(11) DEFAULT NULL COMMENT '所属ESG',
  `elb_id` int(11) DEFAULT NULL COMMENT '所属ELB',
  `host_name` varchar(45) DEFAULT NULL COMMENT 'PCS、ECS主机名，按命名规则录入，用于存入OneCMDB',
  `server_alias` varchar(45) DEFAULT NULL COMMENT 'PCS所在的物理机别名，来源于OneCMDB',
  `host_server_alias` varchar(45) DEFAULT NULL COMMENT 'ECS所在的宿主机别名，来源于OneCMDB',
  `os_storage_alias` varchar(45) DEFAULT NULL COMMENT 'ECS所在的OS卷别名，来源于OneCMDB',
  PRIMARY KEY (`id`),
  KEY `fk_compute_item_apply1_idx` (`apply_id`),
  KEY `fk_compute_item_network_esg_item1_idx` (`esg_id`),
  KEY `fk_compute_item_network_elb_item1_idx` (`elb_id`),
  CONSTRAINT `fk_compute_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_compute_item_network_esg_item1` FOREIGN KEY (`esg_id`) REFERENCES `network_esg_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_compute_item_network_elb_item1` FOREIGN KEY (`elb_id`) REFERENCES `network_elb_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='计算资源（虚拟机实例+物理机）明细表';

/*Data for the table `compute_item` */

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

/*Table structure for table `dictionary` */

DROP TABLE IF EXISTS `dictionary`;

CREATE TABLE `dictionary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL COMMENT '类型：1-',
  `name` varchar(45) NOT NULL COMMENT '名称',
  `value` varchar(45) NOT NULL COMMENT '值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典、参数配置表';

/*Data for the table `dictionary` */

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
  `source_port` varchar(45) NOT NULL COMMENT '源端口',
  `target_port` varchar(45) NOT NULL COMMENT '实例端口',
  PRIMARY KEY (`id`),
  KEY `fk_eip_port_item_network_eip_item1_idx` (`eip_id`),
  CONSTRAINT `fk_eip_port_item_network_eip_item1` FOREIGN KEY (`eip_id`) REFERENCES `network_eip_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='EIP端口映射明细表';

/*Data for the table `eip_port_item` */

/*Table structure for table `elb_port_item` */

DROP TABLE IF EXISTS `elb_port_item`;

CREATE TABLE `elb_port_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `elb_id` int(11) NOT NULL,
  `protocol` varchar(45) NOT NULL COMMENT '网络协议',
  `source_port` varchar(45) NOT NULL COMMENT '负载均衡器端口',
  `target_port` varchar(45) NOT NULL COMMENT '实例端口',
  PRIMARY KEY (`id`),
  KEY `fk_elb_port_item_network_elb_item1_idx` (`elb_id`),
  CONSTRAINT `fk_elb_port_item_network_elb_item1` FOREIGN KEY (`elb_id`) REFERENCES `network_elb_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ELB端口映射明细表';

/*Data for the table `elb_port_item` */

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ESG规则明细表';

/*Data for the table `esg_rule_item` */

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
  CONSTRAINT `fk_fault_user10` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_fault_redmine_issue10` FOREIGN KEY (`redmine_issue_id`) REFERENCES `redmine_issue` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='故障申报表';

/*Data for the table `failure` */

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

insert  into `group_permission`(`group_id`,`permission`) values (1,'user:view'),(1,'group:view'),(2,'user:view');

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
  `pool_type` int(11) NOT NULL COMMENT 'IP池类型：1-私网IP池；2-公共资源访问IP池（存储、负载、防火墙）；3-互联网访问IP池，对应VLAN：电信；联通',
  `vlan_id` int(11) NOT NULL,
  `ip_address` varchar(45) NOT NULL COMMENT 'IP地址',
  `status` int(11) NOT NULL COMMENT '状态：1-未使用；2-已占用（申请保存前，后台定时恢复过期的）；3-已申请（审批完成前）；4-已使用（审批结束）',
  `host_server_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ip_pool_host_server1_idx` (`host_server_id`),
  KEY `fk_ip_pool_vlan1_idx` (`vlan_id`),
  CONSTRAINT `fk_ip_pool_host_server1` FOREIGN KEY (`host_server_id`) REFERENCES `host_server` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ip_pool_vlan1` FOREIGN KEY (`vlan_id`) REFERENCES `vlan` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='IP池表';

/*Data for the table `ip_pool` */

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
  CONSTRAINT `fk_mdn_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
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

/*Table structure for table `monitor_compute` */

DROP TABLE IF EXISTS `monitor_compute`;

CREATE TABLE `monitor_compute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='监控服务计算资源参数表';

/*Data for the table `monitor_compute` */

/*Table structure for table `monitor_elb` */

DROP TABLE IF EXISTS `monitor_elb`;

CREATE TABLE `monitor_elb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `elb_id` int(11) NOT NULL,
  `identifier` varchar(45) NOT NULL COMMENT '标识符',
  PRIMARY KEY (`id`),
  KEY `fk_monitor_storage_apply1_idx` (`apply_id`),
  KEY `fk_monitor_elb_network_elb_item1_idx` (`elb_id`),
  CONSTRAINT `fk_monitor_storage_apply10` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_monitor_elb_network_elb_item1` FOREIGN KEY (`elb_id`) REFERENCES `network_elb_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='监控服务ELB资源参数表';

/*Data for the table `monitor_elb` */

/*Table structure for table `monitor_mail` */

DROP TABLE IF EXISTS `monitor_mail`;

CREATE TABLE `monitor_mail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `email` varchar(45) NOT NULL COMMENT '邮件',
  PRIMARY KEY (`id`),
  KEY `fk_monitor_mail_apply1_idx` (`apply_id`),
  CONSTRAINT `fk_monitor_mail_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='监控服务邮件表';

/*Data for the table `monitor_mail` */

/*Table structure for table `monitor_phone` */

DROP TABLE IF EXISTS `monitor_phone`;

CREATE TABLE `monitor_phone` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `telephone` varchar(45) NOT NULL COMMENT '手机号',
  PRIMARY KEY (`id`),
  KEY `fk_monitor_phone_apply1_idx` (`apply_id`),
  CONSTRAINT `fk_monitor_phone_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='监控服务电话表';

/*Data for the table `monitor_phone` */

/*Table structure for table `network_dns_item` */

DROP TABLE IF EXISTS `network_dns_item`;

CREATE TABLE `network_dns_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `identifier` varchar(45) NOT NULL COMMENT '标识符',
  `domain_name` varchar(45) NOT NULL COMMENT '域名',
  `domain_type` int(11) NOT NULL COMMENT '域名类型：1-GSLB；2-A；3-CNAME',
  `cname_domain` varchar(45) DEFAULT NULL COMMENT 'CNAME域名，若域名类型为CNAME，则需填写此字段。',
  PRIMARY KEY (`id`),
  KEY `fk_network_dns_item_apply1_idx` (`apply_id`),
  CONSTRAINT `fk_network_dns_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网络资源DNS明细表';

/*Data for the table `network_dns_item` */

/*Table structure for table `network_eip_item` */

DROP TABLE IF EXISTS `network_eip_item`;

CREATE TABLE `network_eip_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `identifier` varchar(45) NOT NULL COMMENT '标识符',
  `isp_type` int(11) NOT NULL COMMENT '运营商类型：1-电信；2-联通；3-移动',
  `ip_address` varchar(45) NOT NULL COMMENT '公网/互联网IP地址',
  `old_ip` varchar(45) NOT NULL COMMENT '初始分配的IP',
  `elb_id` int(11) DEFAULT NULL COMMENT '关联ELB',
  `compute_id` int(11) DEFAULT NULL COMMENT '关联计算资源',
  PRIMARY KEY (`id`),
  KEY `fk_network_eip_item_apply1_idx` (`apply_id`),
  KEY `fk_network_eip_item_network_elb_item1_idx` (`elb_id`),
  KEY `fk_network_eip_item_compute_item1_idx` (`compute_id`),
  CONSTRAINT `fk_network_eip_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_network_eip_item_network_elb_item1` FOREIGN KEY (`elb_id`) REFERENCES `network_elb_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_network_eip_item_compute_item1` FOREIGN KEY (`compute_id`) REFERENCES `compute_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网络资源EIP明细表';

/*Data for the table `network_eip_item` */

/*Table structure for table `network_elb_item` */

DROP TABLE IF EXISTS `network_elb_item`;

CREATE TABLE `network_elb_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `identifier` varchar(45) NOT NULL COMMENT '标识符',
  `virtual_ip` varchar(45) NOT NULL COMMENT '负载均衡虚拟IP，从IP池获取',
  `keep_session` tinyint(1) NOT NULL COMMENT '是否保持会话：1-是；0-否',
  PRIMARY KEY (`id`),
  KEY `fk_in_vpn_item_apply1_idx` (`apply_id`),
  CONSTRAINT `fk_in_vpn_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网络资源ELB明细表';

/*Data for the table `network_elb_item` */

/*Table structure for table `network_esg_item` */

DROP TABLE IF EXISTS `network_esg_item`;

CREATE TABLE `network_esg_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID，如果为空则表示公共的',
  `identifier` varchar(45) NOT NULL COMMENT '标识符',
  `description` varchar(45) NOT NULL COMMENT '安全组的描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网络资源ESG明细表';

/*Data for the table `network_esg_item` */

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
  `resource_id` varchar(255) DEFAULT NULL COMMENT '该工单关联的资源ID（用于下线回收操作），多个ID以","隔开',
  `apply_id` int(11) DEFAULT NULL COMMENT '服务申请ID，服务申请时写入',
  `service_tag_id` int(11) DEFAULT NULL COMMENT '服务标签ID，服务变更时写入',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工单处理表（同步Redmine中的Issue表）';

/*Data for the table `redmine_issue` */

/*Table structure for table `resources` */

DROP TABLE IF EXISTS `resources`;

CREATE TABLE `resources` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '所属申请人',
  `service_type` int(11) NOT NULL COMMENT '各个单项的服务类型：1-PCS、2-ECS、3-ES3、4-ELB、5-EIP、6-DNS、7-ESG、8-MDN、9-MonitorCompute、10-MonitorELB',
  `service_tag_id` int(11) NOT NULL COMMENT '服务标签',
  `service_id` int(11) NOT NULL COMMENT '当前各个单项服务的主键ID',
  `service_identifier` varchar(45) NOT NULL COMMENT '原各单项服务的标识符',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `status` int(11) NOT NULL COMMENT '状态：-1-未变更（目前是走完审批流程，以后可能是创建完成才能变更）；0-已变更（未提交）；1-待审批（已提交）；2-审批中；3-已退回；4-已审批；5-创建中；6-已创建；7-回收中',
  `ip_address` varchar(45) DEFAULT NULL COMMENT 'IP地址',
  `usedby` int(11) DEFAULT NULL COMMENT '运维人ID',
  PRIMARY KEY (`id`),
  KEY `fk_resources_service_tag1_idx` (`service_tag_id`),
  CONSTRAINT `fk_resources_service_tag1` FOREIGN KEY (`service_tag_id`) REFERENCES `service_tag` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源总表（服务申请时向此表写入记录，服务变更时更新此记录的新申请ID和新服务ID）';

/*Data for the table `resources` */

/*Table structure for table `service_tag` */

DROP TABLE IF EXISTS `service_tag`;

CREATE TABLE `service_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `name` varchar(45) NOT NULL COMMENT '服务标签名称（或网台名称）',
  `service_type` int(11) NOT NULL COMMENT '大服务类型：1-基础设施；2-MDN；3-云生产；4-监控',
  `priority` int(11) NOT NULL COMMENT '优先级：2-普通；3-高；4-紧急',
  `description` varchar(500) NOT NULL COMMENT '描述用途等',
  `service_start` varchar(10) NOT NULL COMMENT '服务起始时间，同apply表',
  `service_end` varchar(10) NOT NULL COMMENT '服务截止时间，同apply表',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自定义服务类型标签表（用于用户自己关联其下所有资源）';

/*Data for the table `service_tag` */

/*Table structure for table `storage_item` */

DROP TABLE IF EXISTS `storage_item`;

CREATE TABLE `storage_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `apply_id` int(11) NOT NULL COMMENT '申请ID',
  `identifier` varchar(45) NOT NULL COMMENT '唯一标识，由后台生成，规则：？',
  `space` int(11) NOT NULL COMMENT '存储空间：以GB为单位，如：20GB',
  `storage_type` int(11) NOT NULL COMMENT '存储类型：1-Fimas(高吞吐量)；2-Netapp(高IOPS)',
  `controller_alias` varchar(45) DEFAULT NULL COMMENT 'OneCMDB中的Fimas、Netapp控制器别名',
  `volume` varchar(45) DEFAULT NULL COMMENT '挂载卷/盘符',
  `mount_point` varchar(45) DEFAULT NULL COMMENT '挂载点',
  PRIMARY KEY (`id`),
  KEY `fk_storage_item_apply1_idx` (`apply_id`),
  CONSTRAINT `fk_storage_item_apply1` FOREIGN KEY (`apply_id`) REFERENCES `apply` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='存储资源明细表';

/*Data for the table `storage_item` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `name` varchar(15) NOT NULL COMMENT '用户姓名',
  `login_name` varchar(45) DEFAULT NULL COMMENT '登录名',
  `password` varchar(45) NOT NULL COMMENT '用户密码',
  `salt` varchar(45) DEFAULT NULL COMMENT '混淆密码',
  `email` varchar(45) NOT NULL COMMENT 'Sobey邮箱',
  `phonenum` varchar(45) NOT NULL COMMENT '联系电话',
  `department_id` int(11) NOT NULL COMMENT '所属部门ID',
  `leader_id` int(11) DEFAULT NULL COMMENT '直属领导ID',
  `type` int(11) NOT NULL COMMENT '用户类型：1-管理员；2-申请人；3-审核人；4-运维人',
  `create_time` datetime NOT NULL COMMENT '注册时间',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态：1-正常；2-无效',
  `redmine_user_id` int(11) DEFAULT NULL COMMENT 'Redmine中的用户ID，运维人员才有。',
  PRIMARY KEY (`id`),
  KEY `fk_user_department1_idx` (`department_id`),
  CONSTRAINT `fk_user_department1` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`login_name`,`password`,`salt`,`email`,`phonenum`,`department_id`,`leader_id`,`type`,`create_time`,`login_time`,`status`,`redmine_user_id`) values (1,'admin','admin','252063d9c119cc991bc8f18cf2cc9c159cb3ee0a','15b136cda2ac0ce4','admin','1',1,NULL,1,'2013-01-26 11:46:53',NULL,1,NULL),(2,'张鹏','zhangpeng01','fd2191b0c321881dda0ba8be9820215ae9626bb5','bb37234d228762aa','zhangpeng01','1',7,3,3,'2013-01-26 11:46:53',NULL,1,NULL),(3,'陈路','chenlu','84477ef8c7f2c9bb7ae41c4ee69a129459a9b56a','5b45b158c9f80e57','chenlu','1',1,NULL,3,'2013-01-26 11:46:53',NULL,1,NULL),(4,'张汨','zhangmi','8c7f7b6cf5c7ce6290f4c25cd9aa54719109239a','bc35b610ed7358e0','zhangmi','1',6,2,3,'2013-01-26 11:46:53',NULL,1,NULL),(5,'陆俊','lujun','bf56594e1121b52d4a7a1cb40ea054b1bd142dbb','b1e180ff71322690','lujun','1',7,2,4,'2013-01-26 11:46:53',NULL,1,3),(6,'余波','yubo01','8b49a1a898a9371684b85a591d93fa97b99aae49','f12d2351510b54ee','yubo01','1',7,2,4,'2013-01-26 11:46:53',NULL,1,4),(7,'艾磊','ailei','c63aa96aec3f4b5b353227550dc83dc27fc4d17e','012bde1a6acd1c85','ailei','1',7,2,5,'2013-01-26 11:46:53',NULL,1,5),(8,'杨飞','yangfei','f745bdf40b6b4c30d037f16f841b0adc818ead3d','703f855567ad67cd','yangfei','1',7,2,5,'2013-01-26 11:46:53',NULL,1,6),(9,'胡光俊','huguangjun','b7514417d9bea0d9d245595569f181b78afc352c','7b692d6aec5a55bd','huguangjun','1',7,2,5,'2013-01-26 11:46:53',NULL,1,7),(10,'李乾星','liqianxing','3d1f02a438a1ceeb7e47365d377850c1241641c5','4f10893d4480a3c9','liqianxing','1',7,2,4,'2013-01-26 11:46:53',NULL,1,8),(11,'刘力铭','liuliming','1655f94c610b39cd58bcb0d2c2bd1d2ae4e0c282','614da572e79326f4','liuliming','1',7,2,4,'2013-01-26 11:46:53',NULL,1,9),(12,'苏颖','suying','08c30ab40fa1c852cbc089222d9d779e9ec9b031','e5da38e20c7f1df2','suying','1',7,2,5,'2013-01-26 11:46:53',NULL,1,10),(13,'温路平','wenlp','58b8eeb497522af136fcbcb775f584a0d5a7b58f','d3408f0b994a645d','wenlp@163.com','1',1,1,2,'2013-01-26 11:46:53',NULL,1,NULL);

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

insert  into `user_group`(`user_id`,`group_id`) values (1,1),(13,1),(2,3),(3,3),(4,3),(5,4),(6,4),(10,4),(11,4),(7,5),(8,5),(9,5),(12,5);

/*Table structure for table `vlan` */

DROP TABLE IF EXISTS `vlan`;

CREATE TABLE `vlan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL COMMENT '名字',
  `description` varchar(45) NOT NULL COMMENT '描述',
  `alias` varchar(45) NOT NULL COMMENT '对应OneCMDB中的Vlans别名',
  PRIMARY KEY (`id`),
  KEY `fk_vlan_location1_idx` (`location_id`),
  CONSTRAINT `fk_vlan_location1` FOREIGN KEY (`location_id`) REFERENCES `location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='VLAN表，对应OneCMDB中的Vlans';

/*Data for the table `vlan` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
