SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `cmop` ;
CREATE SCHEMA IF NOT EXISTS `cmop` DEFAULT CHARACTER SET utf8 ;
USE `cmop` ;

-- -----------------------------------------------------
-- Table `cmop`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`user` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '用户ID' ,
  `name` VARCHAR(10) NOT NULL COMMENT '用户姓名' ,
  `password` VARCHAR(20) NOT NULL COMMENT '用户密码' ,
  `email` VARCHAR(45) NOT NULL COMMENT 'Sobey邮箱（登录名）' ,
  `phonenum` VARCHAR(45) NOT NULL COMMENT '联系电话' ,
  `department` INT NOT NULL COMMENT '所属部门ID：1-新媒体产品部；2-新媒体项目部...' ,
  `leader_id` INT NULL COMMENT '直属领导ID' ,
  `type` CHAR(1) NOT NULL COMMENT '用户类型：1-管理员；2-申请人；3-审核人' ,
  `create_time` DATETIME NOT NULL COMMENT '注册时间' ,
  `login_time` DATETIME NULL COMMENT '登录时间' ,
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态：1-正常；2-无效' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = '用户表';


-- -----------------------------------------------------
-- Table `cmop`.`apply`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`apply` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`apply` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '申请/变更ID' ,
  `user_id` INT NOT NULL COMMENT '用户ID' ,
  `service_type` VARCHAR(45) NOT NULL COMMENT '服务类型：ECS、ES3...，若是组合，以“-”隔开' ,
  `resource_type` INT NOT NULL COMMENT '资源类型：1-生产资源；2-测试资源；3-公测资源' ,
  `title` VARCHAR(45) NOT NULL COMMENT '标题，自动生成规则：用户-服务类型-创建时间（YYYYMMDDHHMMSS）' ,
  `description` VARCHAR(100) NOT NULL COMMENT '用途描述' ,
  `service_start` VARCHAR(10) NOT NULL COMMENT '服务开始时间' ,
  `service_end` VARCHAR(10) NOT NULL COMMENT '服务结束时间' ,
  `create_time` DATETIME NOT NULL COMMENT '申请/变更时间' ,
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态：1-待审核；2-审核中；3-已退回；4-已审核' ,
  `redmine_issue_id` INT NULL COMMENT '关联Redmine的ID' ,
  `apply_id` INT NULL COMMENT '如果是变更记录，则需保存关联的申请ID' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_apply_user1` (`user_id` ASC) ,
  CONSTRAINT `fk_apply_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `cmop`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '资源申请表';


-- -----------------------------------------------------
-- Table `cmop`.`in_vpn_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`in_vpn_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`in_vpn_item` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '明细ID' ,
  `apply_id` INT NOT NULL COMMENT '申请ID' ,
  `in_type` INT NOT NULL COMMENT '接入类型，根据所选资源类型取值：1-生成资源对应VPN接入；2-测试公测资源对应XenApp接入' ,
  `account` VARCHAR(45) NOT NULL COMMENT 'VPN账号' ,
  `account_user` VARCHAR(45) NOT NULL COMMENT '使用人' ,
  `visit_host` VARCHAR(45) NOT NULL COMMENT '需要访问主机' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_in_vpn_item_apply1` (`apply_id` ASC) ,
  CONSTRAINT `fk_in_vpn_item_apply1`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'VPN接入服务明细表';


-- -----------------------------------------------------
-- Table `cmop`.`network_port_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`network_port_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`network_port_item` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '明细ID' ,
  `apply_id` INT NOT NULL COMMENT '申请ID' ,
  `service_port` VARCHAR(50) NOT NULL COMMENT '服务-端口，如：FTP-21' ,
  INDEX `fk_network_port_apply1` (`apply_id` ASC) ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_network_port_apply1`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '网络开放端口明细表';


-- -----------------------------------------------------
-- Table `cmop`.`network_domain_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`network_domain_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`network_domain_item` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '明细ID' ,
  `apply_id` INT NOT NULL COMMENT '申请ID' ,
  `analyse_type` INT NOT NULL COMMENT '解析类型：1-NS；2-MX；3-A；4-CNAME' ,
  `domain` VARCHAR(45) NOT NULL COMMENT '要解析的完整域名' ,
  `ip` VARCHAR(45) NOT NULL COMMENT '目标IP地址' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_network_domain_item_apply1` (`apply_id` ASC) ,
  CONSTRAINT `fk_network_domain_item_apply1`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '网络域名解析明细表';


-- -----------------------------------------------------
-- Table `cmop`.`audit_flow`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`audit_flow` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`audit_flow` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '审批流程ID' ,
  `flow_type` INT NOT NULL COMMENT '流程类型：1-资源申请/变更的审批流程' ,
  `user_id` INT NOT NULL COMMENT '审批人' ,
  `audit_order` CHAR(1) NOT NULL COMMENT '审批顺序：1-直属领导审批；2...-上级领导审批' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_audit_flow_user1` (`user_id` ASC) ,
  CONSTRAINT `fk_audit_flow_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `cmop`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '审批流程表';


-- -----------------------------------------------------
-- Table `cmop`.`audit`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`audit` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`audit` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '审批ID' ,
  `apply_id` INT NOT NULL COMMENT '申请ID' ,
  `audit_flow_id` INT NOT NULL ,
  `create_time` DATETIME NOT NULL COMMENT '审批时间' ,
  `result` CHAR(1) NOT NULL COMMENT '审批结果：1-同意；2-不同意但继续；3-不同意且退回' ,
  `opinion` VARCHAR(45) NULL COMMENT '审批意见' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_audit_apply1` (`apply_id` ASC) ,
  INDEX `fk_audit_audit_flow1` (`audit_flow_id` ASC) ,
  CONSTRAINT `fk_audit_apply1`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_audit_audit_flow1`
    FOREIGN KEY (`audit_flow_id` )
    REFERENCES `cmop`.`audit_flow` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '审批表';


-- -----------------------------------------------------
-- Table `cmop`.`fault`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`fault` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`fault` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '申报ID' ,
  `user_id` INT NOT NULL COMMENT '用户ID' ,
  `title` VARCHAR(45) NOT NULL COMMENT '标题，自动生成规则：用户-故障申报-创建时间（YYYYMMDDHHMMSS）' ,
  `level` INT NOT NULL COMMENT '优先级：1-低；2-普通；3-高；4-紧急；5-立刻' ,
  `description` VARCHAR(500) NOT NULL COMMENT '故障描述' ,
  `create_time` DATETIME NOT NULL COMMENT '申报时间' ,
  `redmine_issue_id` INT NULL COMMENT '关联Redmine的ID' ,
  `redmine_status` INT NULL COMMENT 'Redmine中的状态：1-已受理；2-已解决' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_fault_user1` (`user_id` ASC) ,
  CONSTRAINT `fk_fault_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `cmop`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '故障申报表';


-- -----------------------------------------------------
-- Table `cmop`.`storage_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`storage_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`storage_item` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '明细ID' ,
  `apply_id` INT NOT NULL COMMENT '申请ID' ,
  `storage_space` INT NOT NULL COMMENT '存储空间：以GB为单位，如：20GB' ,
  `storage_type` INT NULL COMMENT '存储类型：1-数据存储；2-业务存储' ,
  `storage_throughput` INT NULL COMMENT '存储吞吐量：1-50Mbps以内；2-50Mbps以上' ,
  `storage_iops` INT NULL COMMENT '存储每秒进行读写（I/O）操作的次数' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_storage_item_apply1` (`apply_id` ASC) ,
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
  INDEX `fk_user_group_group1` (`group_id` ASC) ,
  INDEX `fk_user_group_user1` (`user_id` ASC) ,
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
  INDEX `fk_group_permission_group1` (`group_id` ASC) ,
  CONSTRAINT `fk_group_permission_group1`
    FOREIGN KEY (`group_id` )
    REFERENCES `cmop`.`group` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '角色-权限关系表';


-- -----------------------------------------------------
-- Table `cmop`.`compute_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`compute_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`compute_item` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '明细ID' ,
  `apply_id` INT NOT NULL COMMENT '申请ID' ,
  `identifier` VARCHAR(45) NOT NULL COMMENT '唯一标识，由后台生成，规则：？' ,
  `os_type` INT NOT NULL COMMENT '操作系统类型：1-Windwos2003R2；2-Windwos2008R2；3-Centos5.6；4-Centos6.3' ,
  `os_bit` INT NOT NULL COMMENT '操作系统位数：1-32bit；2-64bit' ,
  `server_type` INT NOT NULL COMMENT '服务器类型：1-Small；2-Middle；3-Large' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_compute_item_apply1` (`apply_id` ASC) ,
  CONSTRAINT `fk_compute_item_apply1`
    FOREIGN KEY (`apply_id` )
    REFERENCES `cmop`.`apply` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '计算资源（实例）明细表';


-- -----------------------------------------------------
-- Table `cmop`.`compute_storage_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmop`.`compute_storage_item` ;

CREATE  TABLE IF NOT EXISTS `cmop`.`compute_storage_item` (
  `compute_item_id` INT NOT NULL COMMENT '计算资源ID' ,
  `storage_item_id` INT NOT NULL COMMENT '存储资源ID' ,
  INDEX `fk_compute_storage_item_compute_item1` (`compute_item_id` ASC) ,
  INDEX `fk_compute_storage_item_storage_item1` (`storage_item_id` ASC) ,
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



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `cmop`.`user`
-- -----------------------------------------------------
START TRANSACTION;
USE `cmop`;
INSERT INTO `cmop`.`user` (`id`, `name`, `password`, `email`, `phonenum`, `department`, `leader_id`, `type`, `create_time`, `login_time`, `status`) VALUES (1, 'admin', 'admin', 'admin@sobey.com', '1', 1, NULL, '1', '2012-07-01 12:00:00', NULL, 1);

COMMIT;

-- -----------------------------------------------------
-- Data for table `cmop`.`group`
-- -----------------------------------------------------
START TRANSACTION;
USE `cmop`;
INSERT INTO `cmop`.`group` (`id`, `name`) VALUES (1, '管理员');

COMMIT;

-- -----------------------------------------------------
-- Data for table `cmop`.`user_group`
-- -----------------------------------------------------
START TRANSACTION;
USE `cmop`;
INSERT INTO `cmop`.`user_group` (`user_id`, `group_id`) VALUES (1, 1);

COMMIT;

-- -----------------------------------------------------
-- Data for table `cmop`.`group_permission`
-- -----------------------------------------------------
START TRANSACTION;
USE `cmop`;
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (1, 'user:view');
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (1, 'user:edit');
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (1, 'group:view');
INSERT INTO `cmop`.`group_permission` (`group_id`, `permission`) VALUES (1, 'group:edit');

COMMIT;
