SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `cmop` ;
CREATE SCHEMA IF NOT EXISTS `cmop` DEFAULT CHARACTER SET utf8 ;
USE `cmop` ;

-- -----------------------------------------------------
-- Table `cmop`.`department`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`department` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`department` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL COMMENT '部门名称' ,
  `pid` VARCHAR(45) NULL COMMENT '父部门ID' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = '部门表';


-- -----------------------------------------------------
-- Table `cmop`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`user` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '用户ID' ,
  `name` VARCHAR(15) NOT NULL COMMENT '用户姓名' ,
  `login_name` VARCHAR(45) NOT NULL COMMENT '登录名' ,
  `password` VARCHAR(45) NOT NULL COMMENT '用户密码' ,
  `salt` VARCHAR(45) NOT NULL COMMENT '混淆密码' ,
  `email` VARCHAR(45) NOT NULL COMMENT 'Sobey邮箱' ,
  `phonenum` VARCHAR(45) NOT NULL COMMENT '联系电话' ,
  `department_id` INT NOT NULL COMMENT '所属部门ID' ,
  `leader_id` INT NULL COMMENT '直属领导ID' ,
  `type` INT NOT NULL COMMENT '用户类型：1-管理员；2-申请人；3-审核人；4-运维人' ,
  `create_time` DATETIME NOT NULL COMMENT '注册时间' ,
  `login_time` DATETIME NULL COMMENT '登录时间' ,
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态：1-正常；2-无效' ,
  `redmine_user_id` INT NULL COMMENT 'Redmine中的用户ID，运维人员才有。' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_user_department1_idx` (`department_id` ASC) ,
  CONSTRAINT `fk_user_department1`
    FOREIGN KEY (`department_id` )
    REFERENCES `cmop`.`department` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '用户表';


-- -----------------------------------------------------
-- Table `cmop`.`redmine_issue`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`redmine_issue` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`redmine_issue` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `issue_id` INT NULL COMMENT 'Redmine中的Issue主键' ,
  `tracker_id` INT NOT NULL COMMENT 'Redmine中的Tracker' ,
  `project_id` INT NOT NULL COMMENT 'Redmine中的Project' ,
  `subject` VARCHAR(255) NOT NULL COMMENT 'Redmine中的subject' ,
  `assignee` INT NOT NULL COMMENT '下一个操作人，默认首先指派给余波' ,
  `status` INT NOT NULL COMMENT '状态，通Redmine中的状态：1-新建；2-处理中；5-已关闭' ,
  `resource_id` VARCHAR(255) NULL COMMENT '该工单关联的资源ID（用于下线回收操作），多个ID以\",\"隔开' ,
  `apply_id` INT NULL COMMENT '服务申请ID，服务申请时写入' ,
  `service_tag_id` INT NULL COMMENT '服务标签ID，服务变更时写入' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = '工单处理表（同步Redmine中的Issue表）';


-- -----------------------------------------------------
-- Table `cmop`.`audit_flow`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`audit_flow` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`audit_flow` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '审批流程ID' ,
  `flow_type` INT NOT NULL COMMENT '流程类型：1-资源申请/变更的审批流程' ,
  `user_id` INT NOT NULL COMMENT '审批人' ,
  `audit_order` INT NOT NULL COMMENT '审批顺序：1-直属领导审批；2...-上级领导审批' ,
  `is_final` TINYINT(1) NOT NULL COMMENT '是否为终审人：1-是；0-否' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_audit_flow_user1_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_audit_flow_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `cmop`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '审批流程表';


-- -----------------------------------------------------
-- Table `cmop`.`apply`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`apply` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`apply` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '申请/变更ID' ,
  `user_id` INT NOT NULL COMMENT '用户ID' ,
  `title` VARCHAR(45) NOT NULL COMMENT '标题，自动生成规则：用户-服务类型-创建时间（YYYYMMDDHHMMSS）' ,
  `service_tag` VARCHAR(45) NOT NULL COMMENT '服务标签' ,
  `service_type` INT NOT NULL COMMENT '大服务类型：1-基础设施；2-MDN；3-云生产；4-监控' ,
  `priority` INT NOT NULL COMMENT '优先级：2-普通；3-高；4-紧急' ,
  `description` VARCHAR(2000) NOT NULL COMMENT '用途描述' ,
  `service_start` VARCHAR(10) NOT NULL COMMENT '服务开始时间' ,
  `service_end` VARCHAR(10) NOT NULL COMMENT '服务结束时间' ,
  `create_time` DATETIME NOT NULL COMMENT '申请/变更时间' ,
  `status` INT NOT NULL COMMENT '状态：0-已申请；1-待审核；2-审核中；3-已退回；4-已审核；5-处理中；6-已创建' ,
  `audit_flow_id` INT NULL COMMENT '当前所在审批流程ID' ,
  `redmine_issue_id` INT NULL COMMENT '关联Redmine的ID' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_apply_user1_idx` (`user_id` ASC) ,
  INDEX `fk_apply_redmine_issue1_idx` (`redmine_issue_id` ASC) ,
  INDEX `fk_apply_audit_flow1_idx` (`audit_flow_id` ASC) ,
  CONSTRAINT `fk_apply_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `cmop`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_apply_redmine_issue1`
    FOREIGN KEY (`redmine_issue_id` )
    REFERENCES `cmop`.`redmine_issue` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_apply_audit_flow1`
    FOREIGN KEY (`audit_flow_id` )
    REFERENCES `cmop`.`audit_flow` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '资源申请表（也同样适合于服务变更，变更时先向此表写入一行新的记录，再向各个单项写入新的记录）';


-- -----------------------------------------------------
-- Table `cmop`.`network_elb_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`network_elb_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`network_elb_item` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '明细ID' ,
  `apply_id` INT NOT NULL COMMENT '申请ID' ,
  `identifier` VARCHAR(45) NOT NULL COMMENT '标识符' ,
  `virtual_ip` VARCHAR(45) NULL COMMENT '负载均衡虚拟IP，从IP池获取' ,
  `old_ip` VARCHAR(45) NULL ,
  `keep_session` TINYINT(1) NOT NULL COMMENT '是否保持会话：1-是；0-否' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_in_vpn_item_apply1_idx` (`apply_id` ASC) ,
  CONSTRAINT `fk_in_vpn_item_apply1`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '网络资源ELB明细表';


-- -----------------------------------------------------
-- Table `cmop`.`compute_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`compute_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`compute_item` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '明细ID' ,
  `apply_id` INT NOT NULL COMMENT '申请ID' ,
  `identifier` VARCHAR(45) NOT NULL COMMENT '唯一标识，由后台生成，规则：ECS的以ecs开头；PCS的以pcs开头' ,
  `compute_type` INT NOT NULL COMMENT '计算资源类型：1-PCS；2-ECS' ,
  `os_type` INT NOT NULL COMMENT '操作系统类型：1-Windwos2003R2；2-Windwos2008R2；3-Centos5.6；4-Centos6.3' ,
  `os_bit` INT NOT NULL COMMENT '操作系统位数：1-32bit；2-64bit' ,
  `server_type` INT NOT NULL COMMENT '服务器类型：ECS有：1-Small；2-Middle；3-Large；PCS的暂定：4-DELL R410；5-DELL R510；6-DELL R710；7-DELL C6100；8-HP DL2000；9-Aisino 6510；10-SO-5201NR' ,
  `remark` VARCHAR(45) NOT NULL COMMENT '备注，说明该实例的用途' ,
  `inner_ip` VARCHAR(45) NULL COMMENT '内网IP，从IP池获取' ,
  `old_ip` VARCHAR(45) NULL COMMENT '初始分配的IP，为了解决工单描述不能被更新但运维人又要求显示最新的IP' ,
  `host_name` VARCHAR(45) NULL COMMENT 'PCS、ECS主机名，按命名规则录入，用于存入OneCMDB' ,
  `server_alias` VARCHAR(45) NULL COMMENT 'PCS所在的物理机别名，来源于OneCMDB' ,
  `host_server_alias` VARCHAR(45) NULL COMMENT 'ECS所在的宿主机别名，来源于OneCMDB' ,
  `os_storage_alias` VARCHAR(45) NULL COMMENT 'ECS所在的OS卷别名，来源于OneCMDB' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_compute_item_apply1_idx` (`apply_id` ASC) ,
  CONSTRAINT `fk_compute_item_apply1`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '计算资源（虚拟机实例+物理机）明细表';


-- -----------------------------------------------------
-- Table `cmop`.`network_eip_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`network_eip_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`network_eip_item` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '明细ID' ,
  `apply_id` INT NOT NULL COMMENT '申请ID' ,
  `identifier` VARCHAR(45) NOT NULL COMMENT '标识符' ,
  `isp_type` INT NOT NULL COMMENT '运营商类型：1-电信；2-联通；3-移动' ,
  `ip_address` VARCHAR(45) NULL COMMENT '公网/互联网IP地址' ,
  `old_ip` VARCHAR(45) NULL COMMENT '初始分配的IP' ,
  `elb_id` INT NULL COMMENT '关联ELB' ,
  `compute_id` INT NULL COMMENT '关联计算资源' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_network_eip_item_apply1_idx` (`apply_id` ASC) ,
  INDEX `fk_network_eip_item_network_elb_item1_idx` (`elb_id` ASC) ,
  INDEX `fk_network_eip_item_compute_item1_idx` (`compute_id` ASC) ,
  CONSTRAINT `fk_network_eip_item_apply1`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_network_eip_item_network_elb_item1`
    FOREIGN KEY (`elb_id` )
    REFERENCES `cmop`.`network_elb_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_network_eip_item_compute_item1`
    FOREIGN KEY (`compute_id` )
    REFERENCES `cmop`.`compute_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '网络资源EIP明细表';


-- -----------------------------------------------------
-- Table `cmop`.`service_tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`service_tag` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`service_tag` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `identifier` VARCHAR(45) NOT NULL COMMENT '标识符：tag-' ,
  `user_id` INT NOT NULL COMMENT '用户ID' ,
  `name` VARCHAR(45) NOT NULL COMMENT '服务标签名称（或网台名称）' ,
  `priority` INT NOT NULL COMMENT '优先级：2-普通；3-高；4-紧急' ,
  `description` VARCHAR(500) NOT NULL COMMENT '描述用途等' ,
  `service_start` VARCHAR(10) NOT NULL COMMENT '服务起始时间，同apply表' ,
  `service_end` VARCHAR(10) NOT NULL COMMENT '服务截止时间，同apply表' ,
  `create_time` DATETIME NOT NULL COMMENT '创建时间' ,
  `status` INT NOT NULL COMMENT '状态：-1-未变更；0-已变更（未提交）；1-待审批（已提交）；2-审批中；3-已退回；4-已审批' ,
  `audit_flow_id` INT NULL COMMENT '当前所在审批流程ID' ,
  `domain` VARCHAR(45) NULL ,
  `contact` VARCHAR(45) NULL ,
  `phonenum` VARCHAR(45) NULL ,
  `redmine_issue_id` INT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_service_tag_audit_flow1_idx` (`audit_flow_id` ASC) ,
  CONSTRAINT `fk_service_tag_audit_flow1`
    FOREIGN KEY (`audit_flow_id` )
    REFERENCES `cmop`.`audit_flow` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '自定义服务类型标签表（用于用户自己关联其下所有资源）';


-- -----------------------------------------------------
-- Table `cmop`.`audit`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`audit` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`audit` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '审批ID' ,
  `apply_id` INT NULL COMMENT '服务申请ID，服务申请审批时写入' ,
  `service_tag_id` INT NULL COMMENT '服务标签ID，服务变更审批时写入' ,
  `audit_flow_id` INT NOT NULL ,
  `create_time` DATETIME NULL COMMENT '审批时间' ,
  `result` VARCHAR(1) NULL COMMENT '审批结果：1-同意；2-不同意但继续；3-不同意且退回' ,
  `opinion` VARCHAR(45) NULL COMMENT '审批意见' ,
  `status` INT NOT NULL COMMENT '状态（为了解决同一个服务标签多次审批无法区分的问题）：1-有效（审批流程未走完）；0-待审批（预先写入，便于查询）；-1-已过期（审批流程已走完或无效，每次新的提交审批将以前的设置为无效）' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_audit_apply1_idx` (`apply_id` ASC) ,
  INDEX `fk_audit_audit_flow1_idx` (`audit_flow_id` ASC) ,
  INDEX `fk_audit_service_tag1_idx` (`service_tag_id` ASC) ,
  CONSTRAINT `fk_audit_apply1`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_audit_audit_flow1`
    FOREIGN KEY (`audit_flow_id` )
    REFERENCES `cmop`.`audit_flow` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_audit_service_tag1`
    FOREIGN KEY (`service_tag_id` )
    REFERENCES `cmop`.`service_tag` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '审批表';


-- -----------------------------------------------------
-- Table `cmop`.`storage_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`storage_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`storage_item` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '明细ID' ,
  `apply_id` INT NOT NULL COMMENT '申请ID' ,
  `identifier` VARCHAR(45) NOT NULL COMMENT '唯一标识，由后台生成，规则：？' ,
  `space` INT NOT NULL COMMENT '存储空间：以GB为单位，如：20GB' ,
  `storage_type` INT NOT NULL COMMENT '存储类型：1-Fimas(高吞吐量)；2-Netapp(高IOPS)' ,
  `controller_alias` VARCHAR(45) NULL COMMENT 'OneCMDB中的Fimas、Netapp控制器别名' ,
  `volume` VARCHAR(45) NULL COMMENT '挂载卷/盘符' ,
  `mount_point` VARCHAR(45) NULL COMMENT '挂载点' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_storage_item_apply1_idx` (`apply_id` ASC) ,
  CONSTRAINT `fk_storage_item_apply1`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '存储资源明细表';


-- -----------------------------------------------------
-- Table `cmop`.`group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`group` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`group` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '组ID' ,
  `name` VARCHAR(20) NOT NULL COMMENT '角色' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = '角色（组）表';


-- -----------------------------------------------------
-- Table `cmop`.`user_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`user_group` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`user_group` (
  `user_id` INT NOT NULL COMMENT '用户ID' ,
  `group_id` INT NOT NULL COMMENT '角色ID' ,
  INDEX `fk_user_group_group1_idx` (`group_id` ASC) ,
  INDEX `fk_user_group_user1_idx` (`user_id` ASC) ,
  PRIMARY KEY (`user_id`, `group_id`) ,
  CONSTRAINT `fk_user_group_group1`
    FOREIGN KEY (`group_id` )
    REFERENCES `cmop`.`group` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_group_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `cmop`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '用户-角色关系表';


-- -----------------------------------------------------
-- Table `cmop`.`group_permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`group_permission` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`group_permission` (
  `group_id` INT NOT NULL COMMENT '角色ID' ,
  `permission` VARCHAR(45) NOT NULL COMMENT '权限' ,
  INDEX `fk_group_permission_group1_idx` (`group_id` ASC) ,
  CONSTRAINT `fk_group_permission_group1`
    FOREIGN KEY (`group_id` )
    REFERENCES `cmop`.`group` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '角色-权限关系表';


-- -----------------------------------------------------
-- Table `cmop`.`compute_storage_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`compute_storage_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`compute_storage_item` (
  `compute_item_id` INT NOT NULL COMMENT '计算资源ID' ,
  `storage_item_id` INT NOT NULL COMMENT '存储资源ID' ,
  INDEX `fk_compute_storage_item_compute_item1_idx` (`compute_item_id` ASC) ,
  INDEX `fk_compute_storage_item_storage_item1_idx` (`storage_item_id` ASC) ,
  PRIMARY KEY (`compute_item_id`, `storage_item_id`) ,
  CONSTRAINT `fk_compute_storage_item_compute_item1`
    FOREIGN KEY (`compute_item_id` )
    REFERENCES `cmop`.`compute_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_compute_storage_item_storage_item1`
    FOREIGN KEY (`storage_item_id` )
    REFERENCES `cmop`.`storage_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '计算资源与存储资源关联表';


-- -----------------------------------------------------
-- Table `cmop`.`network_esg_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`network_esg_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`network_esg_item` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '明细ID' ,
  `user_id` INT NULL COMMENT '用户ID，如果为空则表示公共的' ,
  `identifier` VARCHAR(45) NOT NULL COMMENT '标识符' ,
  `description` VARCHAR(45) NOT NULL COMMENT '安全组的描述' ,
  `share` TINYINT NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = '网络资源ESG明细表';


-- -----------------------------------------------------
-- Table `cmop`.`eip_port_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`eip_port_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`eip_port_item` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID' ,
  `eip_id` INT NOT NULL COMMENT 'EIP' ,
  `protocol` VARCHAR(45) NOT NULL COMMENT '协议' ,
  `source_port` VARCHAR(45) NOT NULL COMMENT '源端口' ,
  `target_port` VARCHAR(45) NOT NULL COMMENT '实例端口' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_eip_port_item_network_eip_item1_idx` (`eip_id` ASC) ,
  CONSTRAINT `fk_eip_port_item_network_eip_item1`
    FOREIGN KEY (`eip_id` )
    REFERENCES `cmop`.`network_eip_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'EIP端口映射明细表';


-- -----------------------------------------------------
-- Table `cmop`.`esg_rule_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`esg_rule_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`esg_rule_item` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID' ,
  `esg_id` INT NOT NULL COMMENT 'ESG' ,
  `protocol` VARCHAR(45) NOT NULL COMMENT '协议，如：TCP、UDP、SSH...' ,
  `port_range` VARCHAR(45) NOT NULL COMMENT '端口范围，如：80，8080-65535' ,
  `visit_source` VARCHAR(45) NOT NULL COMMENT '访问来源IP，如：192.168.0.1/10，默认：0.0.0.0/0' ,
  `visit_target` VARCHAR(45) NULL COMMENT '访问目的IP，如：192.168.0.1/10，默认：0.0.0.0/0' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_esg_rule_item_network_esg_item1_idx` (`esg_id` ASC) ,
  CONSTRAINT `fk_esg_rule_item_network_esg_item1`
    FOREIGN KEY (`esg_id` )
    REFERENCES `cmop`.`network_esg_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'ESG规则明细表';


-- -----------------------------------------------------
-- Table `cmop`.`elb_port_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`elb_port_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`elb_port_item` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `elb_id` INT NOT NULL ,
  `protocol` VARCHAR(45) NOT NULL COMMENT '网络协议' ,
  `source_port` VARCHAR(45) NOT NULL COMMENT '负载均衡器端口' ,
  `target_port` VARCHAR(45) NOT NULL COMMENT '实例端口' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_elb_port_item_network_elb_item1_idx` (`elb_id` ASC) ,
  CONSTRAINT `fk_elb_port_item_network_elb_item1`
    FOREIGN KEY (`elb_id` )
    REFERENCES `cmop`.`network_elb_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'ELB端口映射明细表';


-- -----------------------------------------------------
-- Table `cmop`.`server_model`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`server_model` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`server_model` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `company` VARCHAR(45) NOT NULL COMMENT '所属公司：HP（惠普）、DELL（戴尔）、Aisino、SO' ,
  `company_alias` VARCHAR(45) NULL ,
  `name` VARCHAR(45) NOT NULL COMMENT '名称' ,
  `cpu` INT NOT NULL COMMENT 'CPU个数' ,
  `memory` INT NOT NULL COMMENT '内存槽数' ,
  `disk` INT NOT NULL COMMENT '硬盘托架数' ,
  `pci` INT NOT NULL COMMENT 'PCI插槽数' ,
  `port` INT NOT NULL COMMENT '网卡口数' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = '服务器型号表';


-- -----------------------------------------------------
-- Table `cmop`.`host_server`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`host_server` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`host_server` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `server_model_id` INT NULL COMMENT '物理机对应的服务器型号' ,
  `server_type` INT NOT NULL COMMENT '类型：1-宿主机、2-物理机' ,
  `pool_type` INT NOT NULL COMMENT 'IP池' ,
  `display_name` VARCHAR(45) NOT NULL COMMENT '显示名（对应OneCMDB中Server的显示名，组成 ：Company Model Rack-Site）' ,
  `rack` VARCHAR(45) NULL COMMENT '机柜位置' ,
  `rack_alias` VARCHAR(45) NULL ,
  `site` VARCHAR(45) NULL COMMENT '模块位置' ,
  `height` VARCHAR(45) NULL COMMENT '高度：1U、2U...' ,
  `alias` VARCHAR(45) NULL COMMENT '别名（对应OneCMDB中Server的别名）' ,
  `location_alias` VARCHAR(45) NULL COMMENT '所在数据中心别名，对应OneCMDB中的Location' ,
  `ip_address` VARCHAR(45) NULL COMMENT '服务器自身IP（宿主机只有一个；物理机可能有多个）' ,
  `nic_site` VARCHAR(45) NULL COMMENT '网卡号' ,
  `switch_site` VARCHAR(45) NULL COMMENT '交换机口' ,
  `switch_alias` VARCHAR(45) NULL COMMENT '交换机alias' ,
  `switch_name` VARCHAR(45) NULL COMMENT '交换机' ,
  `management_mac` VARCHAR(45) NULL COMMENT 'Mac地址' ,
  `create_time` DATETIME NOT NULL COMMENT '创建时间' ,
  `description` VARCHAR(100) NULL ,
  `management_ip` VARCHAR(45) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_host_server_server_model1_idx` (`server_model_id` ASC) ,
  CONSTRAINT `fk_host_server_server_model1`
    FOREIGN KEY (`server_model_id` )
    REFERENCES `cmop`.`server_model` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '宿主机、物理机表';


-- -----------------------------------------------------
-- Table `cmop`.`location`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`location` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`location` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL COMMENT '名字' ,
  `alias` VARCHAR(45) NOT NULL COMMENT '对应OneCMDB中的Location别名' ,
  `city` VARCHAR(45) NULL COMMENT '城市' ,
  `address` VARCHAR(45) NULL COMMENT '地址' ,
  `postcode` VARCHAR(45) NULL COMMENT '邮编' ,
  `telephone` VARCHAR(45) NULL COMMENT '电话' ,
  `create_time` DATETIME NOT NULL COMMENT '创建时间' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = 'IDC表，对应OneCMDB中的Location';


-- -----------------------------------------------------
-- Table `cmop`.`vlan`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`vlan` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`vlan` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `location_id` INT NOT NULL ,
  `name` VARCHAR(45) NOT NULL COMMENT '名字' ,
  `description` VARCHAR(45) NOT NULL COMMENT '描述' ,
  `alias` VARCHAR(45) NOT NULL COMMENT '对应OneCMDB中的Vlans别名' ,
  `create_time` DATETIME NOT NULL COMMENT '创建时间' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_vlan_location1_idx` (`location_id` ASC) ,
  CONSTRAINT `fk_vlan_location1`
    FOREIGN KEY (`location_id` )
    REFERENCES `cmop`.`location` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'VLAN表，对应OneCMDB中的Vlans';


-- -----------------------------------------------------
-- Table `cmop`.`ip_pool`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`ip_pool` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`ip_pool` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `pool_type` INT NOT NULL COMMENT 'IP池类型：1-私网IP池；2-公共资源访问IP池（存储、负载、防火墙）；3-互联网访问IP池，对应VLAN：电信；联通' ,
  `vlan_id` INT NOT NULL ,
  `ip_address` VARCHAR(45) NOT NULL COMMENT 'IP地址' ,
  `status` INT NOT NULL COMMENT '状态：1-未使用；2-已占用（申请保存前，后台定时恢复过期的）；3-已申请（审批完成前）；4-已使用（审批结束）' ,
  `host_server_id` INT NULL ,
  `create_time` DATETIME NOT NULL COMMENT '创建时间' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_ip_pool_host_server1_idx` (`host_server_id` ASC) ,
  INDEX `fk_ip_pool_vlan1_idx` (`vlan_id` ASC) ,
  CONSTRAINT `fk_ip_pool_host_server1`
    FOREIGN KEY (`host_server_id` )
    REFERENCES `cmop`.`host_server` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ip_pool_vlan1`
    FOREIGN KEY (`vlan_id` )
    REFERENCES `cmop`.`vlan` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'IP池表';


-- -----------------------------------------------------
-- Table `cmop`.`mdn_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`mdn_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`mdn_item` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `apply_id` INT NULL COMMENT '服务申请ID，服务申请时写入' ,
  `identifier` VARCHAR(45) NOT NULL COMMENT '标识符：mdn-' ,
  `cover_area` VARCHAR(45) NOT NULL COMMENT '重点覆盖地域' ,
  `cover_isp` VARCHAR(45) NOT NULL COMMENT '重点覆盖ISP' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_mdn_item_apply1_idx` (`apply_id` ASC) ,
  CONSTRAINT `fk_mdn_item_apply1`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'MDN服务申请表';


-- -----------------------------------------------------
-- Table `cmop`.`mdn_vod_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`mdn_vod_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`mdn_vod_item` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `mdn_item_id` INT NOT NULL ,
  `vod_domain` VARCHAR(100) NOT NULL COMMENT '点播服务域名' ,
  `vod_bandwidth` VARCHAR(45) NOT NULL COMMENT '点播加速服务带宽（含单位）' ,
  `vod_protocol` VARCHAR(45) NOT NULL COMMENT '播放协议：http,rtsp' ,
  `source_streamer_url` VARCHAR(100) NOT NULL COMMENT '源站Streamer公网地址' ,
  `source_out_bandwidth` VARCHAR(45) NOT NULL COMMENT '源站出口带宽（含单位）' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_mdn_vod_item_mdn_item1_idx` (`mdn_item_id` ASC) ,
  CONSTRAINT `fk_mdn_vod_item_mdn_item1`
    FOREIGN KEY (`mdn_item_id` )
    REFERENCES `cmop`.`mdn_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'MDN-点播信息表';


-- -----------------------------------------------------
-- Table `cmop`.`network_dns_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`network_dns_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`network_dns_item` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `apply_id` INT NOT NULL COMMENT '申请ID' ,
  `identifier` VARCHAR(45) NOT NULL COMMENT '标识符' ,
  `domain_name` VARCHAR(45) NOT NULL COMMENT '域名' ,
  `domain_type` INT NOT NULL COMMENT '域名类型：1-GSLB；2-A；3-CNAME' ,
  `cname_domain` VARCHAR(45) NULL COMMENT 'CNAME域名，若域名类型为CNAME，则需填写此字段。' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_network_dns_item_apply1_idx` (`apply_id` ASC) ,
  CONSTRAINT `fk_network_dns_item_apply1`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '网络资源DNS明细表';


-- -----------------------------------------------------
-- Table `cmop`.`dns_eip_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`dns_eip_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`dns_eip_item` (
  `dns_item_id` INT NOT NULL ,
  `eip_item_id` INT NOT NULL ,
  INDEX `fk_dns_eip_item_network_dns_item1_idx` (`dns_item_id` ASC) ,
  INDEX `fk_dns_eip_item_network_eip_item1_idx` (`eip_item_id` ASC) ,
  PRIMARY KEY (`dns_item_id`, `eip_item_id`) ,
  CONSTRAINT `fk_dns_eip_item_network_dns_item1`
    FOREIGN KEY (`dns_item_id` )
    REFERENCES `cmop`.`network_dns_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_dns_eip_item_network_eip_item1`
    FOREIGN KEY (`eip_item_id` )
    REFERENCES `cmop`.`network_eip_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'DNS与EIP关联表';


-- -----------------------------------------------------
-- Table `cmop`.`resources`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`resources` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`resources` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `user_id` INT NOT NULL COMMENT '所属申请人' ,
  `service_type` INT NOT NULL COMMENT '各个单项的服务类型：1-PCS、2-ECS、3-ES3、4-ELB、5-EIP、6-DNS、7-ESG、8-MDN、9-MonitorCompute、10-MonitorELB' ,
  `service_tag_id` INT NOT NULL COMMENT '服务标签' ,
  `service_id` INT NOT NULL COMMENT '当前各个单项服务的主键ID' ,
  `service_identifier` VARCHAR(45) NOT NULL COMMENT '原各单项服务的标识符' ,
  `create_time` DATETIME NOT NULL COMMENT '创建时间' ,
  `status` INT NOT NULL COMMENT '状态：-1-未变更（目前是走完审批流程，以后可能是创建完成才能变更）；0-已变更（未提交）；1-待审批（已提交）；2-审批中；3-已退回；4-已审批；5-创建中；6-已创建；7-回收中' ,
  `ip_address` VARCHAR(45) NULL COMMENT 'IP地址' ,
  `old_ip` VARCHAR(45) NULL ,
  `usedby` INT NULL COMMENT '运维人ID' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_resources_service_tag1_idx` (`service_tag_id` ASC) ,
  CONSTRAINT `fk_resources_service_tag1`
    FOREIGN KEY (`service_tag_id` )
    REFERENCES `cmop`.`service_tag` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '资源总表（服务申请时向此表写入记录，服务变更时更新此记录的新申请ID和新服务ID）';


-- -----------------------------------------------------
-- Table `cmop`.`application`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`application` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`application` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `compute_item_id` INT NOT NULL ,
  `name` VARCHAR(45) NOT NULL COMMENT '应用名称' ,
  `version` VARCHAR(45) NOT NULL COMMENT '版本' ,
  `deploy_path` VARCHAR(45) NOT NULL COMMENT '部署路径' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_application_compute_item1_idx` (`compute_item_id` ASC) ,
  CONSTRAINT `fk_application_compute_item1`
    FOREIGN KEY (`compute_item_id` )
    REFERENCES `cmop`.`compute_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '应用表';


-- -----------------------------------------------------
-- Table `cmop`.`change`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`change` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`change` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `resources_id` INT NOT NULL COMMENT '资源ID' ,
  `sub_resources_id` INT NULL COMMENT '服务子项ID，解决MDN其下具体是哪个子项变更的问题' ,
  `user_id` INT NOT NULL COMMENT '变更人' ,
  `change_time` DATETIME NOT NULL COMMENT '变更时间' ,
  `description` VARCHAR(200) NULL COMMENT '变更描述' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_change_item_resources1_idx` (`resources_id` ASC) ,
  CONSTRAINT `fk_change_item_resources1`
    FOREIGN KEY (`resources_id` )
    REFERENCES `cmop`.`resources` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '服务变更表';


-- -----------------------------------------------------
-- Table `cmop`.`attachment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`attachment` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`attachment` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '附件ID' ,
  `file_name` VARCHAR(45) NOT NULL COMMENT '文件名' ,
  `description` VARCHAR(45) NULL COMMENT '文件描述' ,
  `create_time` DATETIME NOT NULL COMMENT '创建时间' ,
  `redmine_issue_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_attachment_redmine_issue1_idx` (`redmine_issue_id` ASC) ,
  CONSTRAINT `fk_attachment_redmine_issue1`
    FOREIGN KEY (`redmine_issue_id` )
    REFERENCES `cmop`.`redmine_issue` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cmop`.`failure`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`failure` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`failure` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '申报ID' ,
  `user_id` INT NOT NULL COMMENT '申报人ID' ,
  `title` VARCHAR(45) NOT NULL COMMENT '标题，自动生成规则：用户-故障申报-创建时间（YYYYMMDDHHMMSS）' ,
  `level` INT NOT NULL COMMENT '优先级：1-低；2-普通；3-高；4-紧急；5-立刻' ,
  `description` VARCHAR(500) NOT NULL COMMENT '故障描述' ,
  `assignee` INT NOT NULL COMMENT '故障受理人ID（从OneCMDB中的Person中读取）' ,
  `fault_type` INT NOT NULL COMMENT '故障类型：1-基础资源；2-MDN；3-VMS；4-云生产' ,
  `related_id` VARCHAR(100) NOT NULL COMMENT '关联的资源ID集合' ,
  `create_time` DATETIME NOT NULL COMMENT '申报时间' ,
  `redmine_issue_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_fault_user1_idx` (`user_id` ASC) ,
  INDEX `fk_fault_redmine_issue1_idx` (`redmine_issue_id` ASC) ,
  CONSTRAINT `fk_fault_user10`
    FOREIGN KEY (`user_id` )
    REFERENCES `cmop`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_fault_redmine_issue10`
    FOREIGN KEY (`redmine_issue_id` )
    REFERENCES `cmop`.`redmine_issue` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '故障申报表';


-- -----------------------------------------------------
-- Table `cmop`.`monitor_mail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`monitor_mail` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`monitor_mail` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `apply_id` INT NOT NULL COMMENT '申请ID' ,
  `user_id` INT NOT NULL COMMENT '用户ID' ,
  `email` VARCHAR(45) NOT NULL COMMENT '邮件' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_monitor_mail_apply1_idx` (`apply_id` ASC) ,
  CONSTRAINT `fk_monitor_mail_apply1`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '监控服务邮件表';


-- -----------------------------------------------------
-- Table `cmop`.`monitor_phone`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`monitor_phone` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`monitor_phone` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `apply_id` INT NOT NULL COMMENT '申请ID' ,
  `user_id` INT NOT NULL COMMENT '用户ID' ,
  `telephone` VARCHAR(45) NOT NULL COMMENT '手机号' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_monitor_phone_apply1_idx` (`apply_id` ASC) ,
  CONSTRAINT `fk_monitor_phone_apply1`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '监控服务电话表';


-- -----------------------------------------------------
-- Table `cmop`.`monitor_compute`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`monitor_compute` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`monitor_compute` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `apply_id` INT NOT NULL COMMENT '申请ID' ,
  `identifier` VARCHAR(45) NOT NULL COMMENT '标识符' ,
  `ip_address` VARCHAR(45) NOT NULL COMMENT '服务器IP' ,
  `cpu_warn` VARCHAR(5) NULL COMMENT 'CPU占用率-报警阀值' ,
  `cpu_critical` VARCHAR(5) NULL COMMENT 'CPU占用率-严重警告阀值' ,
  `memory_warn` VARCHAR(5) NULL COMMENT '内存占用率-报警阀值' ,
  `memory_critical` VARCHAR(5) NULL COMMENT '内存占用率-严重警告阀值' ,
  `disk_warn` VARCHAR(5) NULL COMMENT '硬盘可用率-报警阀值' ,
  `disk_critical` VARCHAR(5) NULL COMMENT '硬盘可用率-严重警告阀值' ,
  `ping_loss_warn` VARCHAR(5) NULL COMMENT '网络丢包率-报警阀值' ,
  `ping_loss_critical` VARCHAR(5) NULL COMMENT '网络丢包率-严重警告阀值' ,
  `ping_delay_warn` VARCHAR(10) NULL COMMENT '网络延时-报警阀值' ,
  `ping_delay_critical` VARCHAR(10) NULL COMMENT '网络延时-严重警告阀值' ,
  `max_process_warn` VARCHAR(5) NULL COMMENT '最大进程数-报警阀值' ,
  `max_process_critical` VARCHAR(5) NULL COMMENT '最大进程数-严重警告阀值' ,
  `port` VARCHAR(200) NULL COMMENT '监控端口，以“,”隔开' ,
  `process` VARCHAR(200) NULL COMMENT '监控进程，以“,”隔开' ,
  `mount_point` VARCHAR(200) NULL COMMENT '挂载路径，以“,”隔开' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_monitor_compute_apply1_idx` (`apply_id` ASC) ,
  CONSTRAINT `fk_monitor_compute_apply1`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '监控服务计算资源参数表';


-- -----------------------------------------------------
-- Table `cmop`.`monitor_elb`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`monitor_elb` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`monitor_elb` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `apply_id` INT NOT NULL COMMENT '申请ID' ,
  `elb_id` INT NOT NULL ,
  `identifier` VARCHAR(45) NOT NULL COMMENT '标识符' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_monitor_storage_apply1_idx` (`apply_id` ASC) ,
  INDEX `fk_monitor_elb_network_elb_item1_idx` (`elb_id` ASC) ,
  CONSTRAINT `fk_monitor_storage_apply10`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_monitor_elb_network_elb_item1`
    FOREIGN KEY (`elb_id` )
    REFERENCES `cmop`.`network_elb_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '监控服务ELB资源参数表';


-- -----------------------------------------------------
-- Table `cmop`.`change_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`change_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`change_item` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `change_id` INT NOT NULL ,
  `field_name` VARCHAR(45) NOT NULL COMMENT '变更项（字段）名称' ,
  `old_value` VARCHAR(200) NOT NULL COMMENT '原值-ID' ,
  `old_string` VARCHAR(500) NULL COMMENT '原值-拼装的字符串' ,
  `new_value` VARCHAR(200) NOT NULL COMMENT '新值-ID' ,
  `new_string` VARCHAR(500) NULL COMMENT '新值-拼装的字符串' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_change_item_change1_idx` (`change_id` ASC) ,
  CONSTRAINT `fk_change_item_change1`
    FOREIGN KEY (`change_id` )
    REFERENCES `cmop`.`change` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '变更明细表';


-- -----------------------------------------------------
-- Table `cmop`.`dictionary`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`dictionary` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`dictionary` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `type` INT NOT NULL COMMENT '类型：1-' ,
  `name` VARCHAR(45) NOT NULL COMMENT '名称' ,
  `value` VARCHAR(45) NOT NULL COMMENT '值' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = '数据字典、参数配置表';


-- -----------------------------------------------------
-- Table `cmop`.`mdn_live_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`mdn_live_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`mdn_live_item` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `mdn_item_id` INT NOT NULL ,
  `live_domain` VARCHAR(100) NOT NULL COMMENT '直播服务域名' ,
  `live_bandwidth` VARCHAR(45) NOT NULL COMMENT '直播加速服务带宽（含单位）' ,
  `live_protocol` VARCHAR(45) NOT NULL COMMENT '播放协议：http,rtsp' ,
  `stream_out_mode` INT NOT NULL COMMENT '直播流输出模式：1-Encoder模式；2-Transfer模式' ,
  `name` VARCHAR(45) NOT NULL COMMENT '频道名称' ,
  `guid` VARCHAR(64) NOT NULL COMMENT '频道GUID' ,
  `bandwidth` VARCHAR(45) NOT NULL COMMENT '出口带宽（含单位）' ,
  `encoder_mode` INT NULL COMMENT '编码器模式：1-HTTP拉流；2-RTMP推流' ,
  `http_url` VARCHAR(100) NULL COMMENT 'HTTP流地址' ,
  `http_bitrate` VARCHAR(45) NULL COMMENT 'HTTP流混合码率' ,
  `hls_url` VARCHAR(100) NULL COMMENT 'M3U8流地址' ,
  `hls_bitrate` VARCHAR(45) NULL COMMENT 'M3U8流混合码率' ,
  `rtsp_url` VARCHAR(100) NULL COMMENT 'RTSP流地址' ,
  `rtsp_bitrate` VARCHAR(45) NULL COMMENT 'RTSP流混合码率' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_mdn_live_item_mdn_item2_idx` (`mdn_item_id` ASC) ,
  CONSTRAINT `fk_mdn_live_item_mdn_item2`
    FOREIGN KEY (`mdn_item_id` )
    REFERENCES `cmop`.`mdn_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'MDN-直播信息表';


-- -----------------------------------------------------
-- Table `cmop`.`cp_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`cp_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`cp_item` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `apply_id` INT NOT NULL ,
  `identifier` VARCHAR(45) NOT NULL COMMENT '标识符：cp-' ,
  `record_stream_url` VARCHAR(100) NOT NULL COMMENT '收录流URL' ,
  `record_bitrate` VARCHAR(10) NOT NULL COMMENT '收录码率' ,
  `export_encode` VARCHAR(200) NOT NULL COMMENT '输出编码，多个以\",\"隔开' ,
  `record_type` INT NOT NULL COMMENT '收录类型：1-每天；2-每周；3-每月' ,
  `record_time` VARCHAR(45) NOT NULL COMMENT '收录时长：时,分,秒' ,
  `publish_url` VARCHAR(100) NULL COMMENT '发布接口地址' ,
  `is_push_ctp` TINYINT(1) NULL COMMENT '是否推送内容交易平台：1-是；0-否' ,
  `video_ftp_ip` VARCHAR(45) NOT NULL COMMENT '视频-FTP上传IP' ,
  `video_ftp_port` VARCHAR(10) NOT NULL COMMENT '视频-FTP端口' ,
  `video_ftp_username` VARCHAR(45) NOT NULL COMMENT '视频-FTP用户名' ,
  `video_ftp_password` VARCHAR(45) NOT NULL COMMENT '视频-FTP密码' ,
  `video_ftp_rootpath` VARCHAR(45) NOT NULL COMMENT '视频-FTP根路径' ,
  `video_ftp_uploadpath` VARCHAR(45) NOT NULL COMMENT '视频-FTP上传路径' ,
  `video_output_group` VARCHAR(100) NOT NULL COMMENT '视频-输出组类型' ,
  `video_output_way` VARCHAR(100) NOT NULL COMMENT '视频-输出方式' ,
  `pictrue_ftp_ip` VARCHAR(45) NOT NULL COMMENT '图片-FTP上传IP' ,
  `pictrue_ftp_port` VARCHAR(10) NOT NULL COMMENT '图片-FTP端口' ,
  `pictrue_ftp_username` VARCHAR(45) NOT NULL COMMENT '图片-FTP用户名' ,
  `pictrue_ftp_password` VARCHAR(45) NOT NULL COMMENT '图片-FTP密码' ,
  `pictrue_ftp_rootpath` VARCHAR(45) NOT NULL COMMENT '图片-FTP根路径' ,
  `pictrue_ftp_uploadpath` VARCHAR(45) NOT NULL COMMENT '图片-FTP上传路径' ,
  `pictrue_output_group` VARCHAR(100) NOT NULL COMMENT '图片-输出组类型' ,
  `pictrue_output_media` VARCHAR(100) NOT NULL COMMENT '图片-输出媒体类型' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_cp_item_apply1_idx` (`apply_id` ASC) ,
  CONSTRAINT `fk_cp_item_apply1`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '云生产服务申请表';


-- -----------------------------------------------------
-- Table `cmop`.`cp_program_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`cp_program_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`cp_program_item` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `cp_item_id` INT NOT NULL ,
  `name` VARCHAR(100) NOT NULL COMMENT '拆条节目单文件名' ,
  `size` INT NOT NULL COMMENT '节目单文件大小，单位：K' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_cp_program_item_cp_item1_idx` (`cp_item_id` ASC) ,
  CONSTRAINT `fk_cp_program_item_cp_item1`
    FOREIGN KEY (`cp_item_id` )
    REFERENCES `cmop`.`cp_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '拆条节目单明细表';


-- -----------------------------------------------------
-- Table `cmop`.`compute_elb_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`compute_elb_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`compute_elb_item` (
  `compute_item_id` INT NOT NULL ,
  `elb_item_id` INT NOT NULL ,
  PRIMARY KEY (`compute_item_id`, `elb_item_id`) ,
  INDEX `fk_table1_network_elb_item1_idx` (`elb_item_id` ASC) ,
  CONSTRAINT `fk_table1_compute_item1`
    FOREIGN KEY (`compute_item_id` )
    REFERENCES `cmop`.`compute_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_table1_network_elb_item1`
    FOREIGN KEY (`elb_item_id` )
    REFERENCES `cmop`.`network_elb_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '计算资源与ELB关联表';


-- -----------------------------------------------------
-- Table `cmop`.`change_history`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`change_history` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`change_history` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `audit_id` INT NOT NULL ,
  `resources_info` VARCHAR(100) NOT NULL COMMENT '资源的主要信息：标识符-IP' ,
  `sub_resources_id` INT NULL COMMENT '服务子项ID，解决MDN其下具体是哪个子项变更的问题' ,
  `change_time` DATETIME NOT NULL COMMENT '变更时间' ,
  `description` VARCHAR(200) NULL COMMENT '变更描述' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_change_history1_audit1_idx` (`audit_id` ASC) ,
  CONSTRAINT `fk_change_Foreign Key: fk_change_history1_audit1`
    FOREIGN KEY (`audit_id` )
    REFERENCES `cmop`.`audit` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '服务变更历史表，用于审批历史查询';


-- -----------------------------------------------------
-- Table `cmop`.`change_item_history`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`change_item_history` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`change_item_history` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `change_history_id` INT NOT NULL ,
  `field_name` VARCHAR(45) NOT NULL COMMENT '变更项（字段）名称' ,
  `old_value` VARCHAR(200) NOT NULL COMMENT '原值-ID' ,
  `old_string` VARCHAR(500) NULL COMMENT '原值-拼装的字符串' ,
  `new_value` VARCHAR(200) NOT NULL COMMENT '新值-ID' ,
  `new_string` VARCHAR(500) NULL COMMENT '新值-拼装的字符串' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_change_item_history_change_history1_idx` (`change_history_id` ASC) ,
  CONSTRAINT `fk_change_item_history_change_history1`
    FOREIGN KEY (`change_history_id` )
    REFERENCES `cmop`.`change_history` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '变更明细历史表';


-- -----------------------------------------------------
-- Table `cmop`.`compute_esg_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`compute_esg_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`compute_esg_item` (
  `compute_item_id` INT NOT NULL ,
  `esg_item_id` INT NOT NULL ,
  PRIMARY KEY (`compute_item_id`, `esg_item_id`) ,
  INDEX `fk_compute_esg_item_network_esg_item1_idx` (`esg_item_id` ASC) ,
  CONSTRAINT `fk_compute_esg_item_compute_item1`
    FOREIGN KEY (`compute_item_id` )
    REFERENCES `cmop`.`compute_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_compute_esg_item_network_esg_item1`
    FOREIGN KEY (`esg_item_id` )
    REFERENCES `cmop`.`network_esg_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '计算资源与ESG关联表';


-- -----------------------------------------------------
-- Table `cmop`.`nic`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`nic` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`nic` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `mac` VARCHAR(45) NULL COMMENT 'mac' ,
  `ip_address` VARCHAR(45) NULL COMMENT '网卡ip' ,
  `alias` VARCHAR(45) NULL COMMENT '网卡alias' ,
  `site` VARCHAR(45) NULL COMMENT '网卡号' ,
  `host_server_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_nic_host_server1` (`host_server_id` ASC) ,
  CONSTRAINT `fk_nic_host_server1`
    FOREIGN KEY (`host_server_id` )
    REFERENCES `cmop`.`host_server` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `cmop` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `cmop`.`department`
-- -----------------------------------------------------
START TRANSACTION;
USE `cmop`;
INSERT INTO `cmop`.`department` (`id`, `name`, `pid`) VALUES (1, '新媒体', NULL);
INSERT INTO `cmop`.`department` (`id`, `name`, `pid`) VALUES (2, '新媒体项目开发部', NULL);
INSERT INTO `cmop`.`department` (`id`, `name`, `pid`) VALUES (3, '新媒体项目服务部', NULL);
INSERT INTO `cmop`.`department` (`id`, `name`, `pid`) VALUES (4, '云平台', NULL);
INSERT INTO `cmop`.`department` (`id`, `name`, `pid`) VALUES (5, '云平台产品部', NULL);
INSERT INTO `cmop`.`department` (`id`, `name`, `pid`) VALUES (6, '云平台研发部', NULL);
INSERT INTO `cmop`.`department` (`id`, `name`, `pid`) VALUES (7, '云平台运维部', NULL);
INSERT INTO `cmop`.`department` (`id`, `name`, `pid`) VALUES (8, '云平台运营部', NULL);
INSERT INTO `cmop`.`department` (`id`, `name`, `pid`) VALUES (9, '广电业务事业部', NULL);
INSERT INTO `cmop`.`department` (`id`, `name`, `pid`) VALUES (10, '广电业务事业部系统产品研发部', NULL);
INSERT INTO `cmop`.`department` (`id`, `name`, `pid`) VALUES (11, '广电业务事业部产品化及支持部', NULL);
INSERT INTO `cmop`.`department` (`id`, `name`, `pid`) VALUES (12, 'MAM产品部', NULL);
INSERT INTO `cmop`.`department` (`id`, `name`, `pid`) VALUES (13, '业务产品开发部', NULL);
INSERT INTO `cmop`.`department` (`id`, `name`, `pid`) VALUES (14, '海外事业部', NULL);
INSERT INTO `cmop`.`department` (`id`, `name`, `pid`) VALUES (15, '海外事业本部基础技术部', NULL);
INSERT INTO `cmop`.`department` (`id`, `name`, `pid`) VALUES (16, '信息管理办', NULL);

COMMIT;

-- -----------------------------------------------------
-- Data for table `cmop`.`user`
-- -----------------------------------------------------
START TRANSACTION;
USE `cmop`;
INSERT INTO `cmop`.`user` (`id`, `name`, `login_name`, `password`, `salt`, `email`, `phonenum`, `department_id`, `leader_id`, `type`, `create_time`, `login_time`, `status`, `redmine_user_id`) VALUES (1, 'admin', 'admin', '252063d9c119cc991bc8f18cf2cc9c159cb3ee0a', '15b136cda2ac0ce4', 'admin', '1', 1, NULL, 1, '2012-07-01 12:00:00', NULL, 1, NULL);
INSERT INTO `cmop`.`user` (`id`, `name`, `login_name`, `password`, `salt`, `email`, `phonenum`, `department_id`, `leader_id`, `type`, `create_time`, `login_time`, `status`, `redmine_user_id`) VALUES (2, '张鹏', 'zhangpeng01', 'fd2191b0c321881dda0ba8be9820215ae9626bb5', 'bb37234d228762aa', 'zhangpeng01', '1', 7, 3, 3, '2012-07-01 12:00:00', NULL, 1, NULL);
INSERT INTO `cmop`.`user` (`id`, `name`, `login_name`, `password`, `salt`, `email`, `phonenum`, `department_id`, `leader_id`, `type`, `create_time`, `login_time`, `status`, `redmine_user_id`) VALUES (3, '陈路', 'chenlu', '84477ef8c7f2c9bb7ae41c4ee69a129459a9b56a', '5b45b158c9f80e57', 'chenlu', '1', 1, NULL, 3, '2012-07-01 12:00:00', NULL, 1, NULL);
INSERT INTO `cmop`.`user` (`id`, `name`, `login_name`, `password`, `salt`, `email`, `phonenum`, `department_id`, `leader_id`, `type`, `create_time`, `login_time`, `status`, `redmine_user_id`) VALUES (4, '张汨', 'zhangmi', '8c7f7b6cf5c7ce6290f4c25cd9aa54719109239a', 'bc35b610ed7358e0', 'zhangmi', '1', 6, 2, 3, '2012-07-01 12:00:00', NULL, 1, NULL);
INSERT INTO `cmop`.`user` (`id`, `name`, `login_name`, `password`, `salt`, `email`, `phonenum`, `department_id`, `leader_id`, `type`, `create_time`, `login_time`, `status`, `redmine_user_id`) VALUES (5, '陆俊', 'lujun', 'bf56594e1121b52d4a7a1cb40ea054b1bd142dbb', 'b1e180ff71322690', 'lujun', '1', 7, 2, 4, '2012-07-01 12:00:00', NULL, 1, 3);
INSERT INTO `cmop`.`user` (`id`, `name`, `login_name`, `password`, `salt`, `email`, `phonenum`, `department_id`, `leader_id`, `type`, `create_time`, `login_time`, `status`, `redmine_user_id`) VALUES (6, '余波', 'yubo01', '8b49a1a898a9371684b85a591d93fa97b99aae49', 'f12d2351510b54ee', 'yubo01', '1', 7, 2, 4, '2012-08-11 12:00:00', NULL, 1, 4);
INSERT INTO `cmop`.`user` (`id`, `name`, `login_name`, `password`, `salt`, `email`, `phonenum`, `department_id`, `leader_id`, `type`, `create_time`, `login_time`, `status`, `redmine_user_id`) VALUES (7, '艾磊', 'ailei', 'c63aa96aec3f4b5b353227550dc83dc27fc4d17e', '012bde1a6acd1c85', 'ailei', '1', 7, 2, 5, '2012-08-11 12:00:00', NULL, 1, 5);
INSERT INTO `cmop`.`user` (`id`, `name`, `login_name`, `password`, `salt`, `email`, `phonenum`, `department_id`, `leader_id`, `type`, `create_time`, `login_time`, `status`, `redmine_user_id`) VALUES (8, '杨飞', 'yangfei', 'f745bdf40b6b4c30d037f16f841b0adc818ead3d', '703f855567ad67cd', 'yangfei', '1', 7, 2, 5, '2012-08-11 12:00:00', NULL, 1, 6);
INSERT INTO `cmop`.`user` (`id`, `name`, `login_name`, `password`, `salt`, `email`, `phonenum`, `department_id`, `leader_id`, `type`, `create_time`, `login_time`, `status`, `redmine_user_id`) VALUES (9, '胡光俊', 'huguangjun', 'b7514417d9bea0d9d245595569f181b78afc352c', '7b692d6aec5a55bd', 'huguangjun', '1', 7, 2, 5, '2012-08-11 12:00:00', NULL, 1, 7);
INSERT INTO `cmop`.`user` (`id`, `name`, `login_name`, `password`, `salt`, `email`, `phonenum`, `department_id`, `leader_id`, `type`, `create_time`, `login_time`, `status`, `redmine_user_id`) VALUES (10, '李乾星', 'liqianxing', '3d1f02a438a1ceeb7e47365d377850c1241641c5', '4f10893d4480a3c9', 'liqianxing', '1', 7, 2, 4, '2012-08-11 12:00:00', NULL, 1, 8);
INSERT INTO `cmop`.`user` (`id`, `name`, `login_name`, `password`, `salt`, `email`, `phonenum`, `department_id`, `leader_id`, `type`, `create_time`, `login_time`, `status`, `redmine_user_id`) VALUES (11, '苏颖', 'suying', '08c30ab40fa1c852cbc089222d9d779e9ec9b031', 'e5da38e20c7f1df2', 'suying', '1', 7, 2, 5, '2012-08-11 12:00:00', NULL, 1, 10);
INSERT INTO `cmop`.`user` (`id`, `name`, `login_name`, `password`, `salt`, `email`, `phonenum`, `department_id`, `leader_id`, `type`, `create_time`, `login_time`, `status`, `redmine_user_id`) VALUES (12, '温路平', 'wenlp', '58b8eeb497522af136fcbcb775f584a0d5a7b58f', 'd3408f0b994a645d', 'wenlp', '1', 6, 4, 2, '2012-08-11 12:00:00', NULL, 1, NULL);

COMMIT;

-- -----------------------------------------------------
-- Data for table `cmop`.`audit_flow`
-- -----------------------------------------------------
START TRANSACTION;
USE `cmop`;
INSERT INTO `cmop`.`audit_flow` (`id`, `flow_type`, `user_id`, `audit_order`, `is_final`) VALUES (1, 1, 2, 2, 0);
INSERT INTO `cmop`.`audit_flow` (`id`, `flow_type`, `user_id`, `audit_order`, `is_final`) VALUES (2, 1, 3, 3, 1);
INSERT INTO `cmop`.`audit_flow` (`id`, `flow_type`, `user_id`, `audit_order`, `is_final`) VALUES (3, 1, 4, 1, 0);

COMMIT;

-- -----------------------------------------------------
-- Data for table `cmop`.`group`
-- -----------------------------------------------------
START TRANSACTION;
USE `cmop`;
INSERT INTO `cmop`.`group` (`id`, `name`) VALUES (1, '管理员');
INSERT INTO `cmop`.`group` (`id`, `name`) VALUES (2, '申请人');
INSERT INTO `cmop`.`group` (`id`, `name`) VALUES (3, '审批人');
INSERT INTO `cmop`.`group` (`id`, `name`) VALUES (4, '运维人A-管理员');
INSERT INTO `cmop`.`group` (`id`, `name`) VALUES (5, '运维人B-操作人');

COMMIT;

-- -----------------------------------------------------
-- Data for table `cmop`.`user_group`
-- -----------------------------------------------------
START TRANSACTION;
USE `cmop`;
INSERT INTO `cmop`.`user_group` (`user_id`, `group_id`) VALUES (1, 1);
INSERT INTO `cmop`.`user_group` (`user_id`, `group_id`) VALUES (2, 3);
INSERT INTO `cmop`.`user_group` (`user_id`, `group_id`) VALUES (3, 3);
INSERT INTO `cmop`.`user_group` (`user_id`, `group_id`) VALUES (4, 3);
INSERT INTO `cmop`.`user_group` (`user_id`, `group_id`) VALUES (5, 4);
INSERT INTO `cmop`.`user_group` (`user_id`, `group_id`) VALUES (6, 4);
INSERT INTO `cmop`.`user_group` (`user_id`, `group_id`) VALUES (7, 5);
INSERT INTO `cmop`.`user_group` (`user_id`, `group_id`) VALUES (8, 5);
INSERT INTO `cmop`.`user_group` (`user_id`, `group_id`) VALUES (9, 5);
INSERT INTO `cmop`.`user_group` (`user_id`, `group_id`) VALUES (10, 4);
INSERT INTO `cmop`.`user_group` (`user_id`, `group_id`) VALUES (11, 5);
INSERT INTO `cmop`.`user_group` (`user_id`, `group_id`) VALUES (12, 2);

COMMIT;

-- -----------------------------------------------------
-- Data for table `cmop`.`group_permission`
-- -----------------------------------------------------
START TRANSACTION;
USE `cmop`;
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (1, 'user:view');
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (1, 'group:view');
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (1, 'department:view');
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (2, 'apply:view');
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (3, 'audit:view');
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (4, 'apply:view');
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (4, 'operate:view');
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (4, 'basicData:view');
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (4, 'summary:view');
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (5, 'apply:view');
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (5, 'operate:view');
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (5, 'basicData:view');
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (5, 'summary:view');

COMMIT;

-- -----------------------------------------------------
-- Data for table `cmop`.`location`
-- -----------------------------------------------------
START TRANSACTION;
USE `cmop`;
INSERT INTO `cmop`.`location` (`id`, `name`, `alias`, `city`, `address`, `postcode`, `telephone`, `create_time`) VALUES (1, '东莞边缘节点', 'Location-1', '东莞', NULL, NULL, NULL, '2013-03-21 00:00:00');
INSERT INTO `cmop`.`location` (`id`, `name`, `alias`, `city`, `address`, `postcode`, `telephone`, `create_time`) VALUES (2, '西安核心数据中心', 'Location-2', '西安', '凤城市十路，经开数据中心', NULL, NULL, '2013-03-21 00:00:00');
INSERT INTO `cmop`.`location` (`id`, `name`, `alias`, `city`, `address`, `postcode`, `telephone`, `create_time`) VALUES (3, '沈阳边缘节点', 'Location-3', '沈阳', NULL, NULL, NULL, '2013-03-21 00:00:00');

COMMIT;
