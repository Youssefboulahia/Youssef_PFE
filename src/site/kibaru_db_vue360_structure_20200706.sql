/*
Navicat MySQL Data Transfer

Source Server         : HABILITATION_MYSQL_PROD
Source Server Version : 50725
Source Host           : localhost:3308
Source Database       : kibaru_db_vue360

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-07-06 11:10:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for action
-- ----------------------------
DROP TABLE IF EXISTS `action`;
CREATE TABLE `action` (
  `id_action` int(10) NOT NULL AUTO_INCREMENT,
  `label_action` varchar(255) NOT NULL,
  `type_action` varchar(255) NOT NULL,
  `system_source` varchar(255) NOT NULL,
  `action_code` varchar(255) NOT NULL,
  `crm_case_subject` varchar(255) DEFAULT NULL,
  `crm_case_category` varchar(255) DEFAULT NULL,
  `crm_case_reason` varchar(255) DEFAULT NULL,
  `tag_action` varchar(255) NOT NULL,
  `ref_module` int(10) NOT NULL,
  `active` tinyint(1) DEFAULT '1',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_action`),
  UNIQUE KEY `tag_action` (`tag_action`),
  KEY `idx_action` (`id_action`,`ref_module`)
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for application
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application` (
  `label_application` varchar(255) NOT NULL,
  `tag_application` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `with_context` tinyint(1) DEFAULT '0',
  `param_context` varchar(255) DEFAULT NULL,
  `id_application` int(10) NOT NULL AUTO_INCREMENT,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_application`),
  UNIQUE KEY `tag_application` (`tag_application`),
  KEY `idx_application` (`active`,`id_application`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for application_module
-- ----------------------------
DROP TABLE IF EXISTS `application_module`;
CREATE TABLE `application_module` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ref_module` int(10) NOT NULL,
  `ref_application` int(10) NOT NULL,
  `active` tinyint(1) DEFAULT '0',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_app_module` (`ref_module`,`ref_application`,`active`)
) ENGINE=InnoDB AUTO_INCREMENT=612 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for groupe
-- ----------------------------
DROP TABLE IF EXISTS `groupe`;
CREATE TABLE `groupe` (
  `id_groupe` int(10) NOT NULL AUTO_INCREMENT,
  `tag_groupe` varchar(255) NOT NULL,
  `label_groupe` varchar(255) NOT NULL,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_groupe`),
  UNIQUE KEY `tag_groupe` (`tag_groupe`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for groupe_page
-- ----------------------------
DROP TABLE IF EXISTS `groupe_page`;
CREATE TABLE `groupe_page` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ref_groupe` int(10) NOT NULL,
  `ref_page` int(10) NOT NULL,
  `active` tinyint(1) DEFAULT '0',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=325 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for info
-- ----------------------------
DROP TABLE IF EXISTS `info`;
CREATE TABLE `info` (
  `id_info` int(10) NOT NULL AUTO_INCREMENT,
  `label_info` varchar(255) NOT NULL,
  `tag_info` varchar(255) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '0',
  `priority` int(10) DEFAULT NULL,
  `ref_rubric` int(10) NOT NULL,
  `system_source` varchar(255) DEFAULT NULL,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `partially_visible` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_info`),
  UNIQUE KEY `tag_info` (`tag_info`),
  KEY `idx_refrubrique_id_info` (`id_info`,`ref_rubric`)
) ENGINE=InnoDB AUTO_INCREMENT=3088 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for level
-- ----------------------------
DROP TABLE IF EXISTS `level`;
CREATE TABLE `level` (
  `id_level` int(10) NOT NULL AUTO_INCREMENT,
  `label_level` varchar(255) NOT NULL,
  `tag_level` varchar(255) NOT NULL,
  `priority` int(11) DEFAULT NULL,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_level`),
  KEY `idx_level` (`id_level`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for level_module
-- ----------------------------
DROP TABLE IF EXISTS `level_module`;
CREATE TABLE `level_module` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ref_module` int(10) NOT NULL,
  `ref_level` int(10) NOT NULL,
  `active` tinyint(1) DEFAULT '0',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2048 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for module
-- ----------------------------
DROP TABLE IF EXISTS `module`;
CREATE TABLE `module` (
  `id_module` int(10) NOT NULL AUTO_INCREMENT,
  `tag_module` varchar(255) NOT NULL,
  `label_module` varchar(255) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `priority` int(10) DEFAULT NULL,
  `with_application` tinyint(1) NOT NULL DEFAULT '1',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_module`),
  UNIQUE KEY `tag_module` (`tag_module`),
  KEY `id_module` (`id_module`),
  KEY `idx_module` (`id_module`,`tag_module`,`active`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for page
-- ----------------------------
DROP TABLE IF EXISTS `page`;
CREATE TABLE `page` (
  `id_page` int(10) NOT NULL AUTO_INCREMENT,
  `tag_page` varchar(255) NOT NULL,
  `label_page` varchar(255) NOT NULL,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_page`),
  UNIQUE KEY `tag_page` (`tag_page`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for profil
-- ----------------------------
DROP TABLE IF EXISTS `profil`;
CREATE TABLE `profil` (
  `id_profil` int(10) NOT NULL AUTO_INCREMENT,
  `tag_profil` varchar(255) NOT NULL,
  `label_profil` varchar(255) NOT NULL,
  `priority` int(10) DEFAULT NULL,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_profil`),
  UNIQUE KEY `tag_profil` (`tag_profil`),
  KEY `id_profils` (`id_profil`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for profil_action
-- ----------------------------
DROP TABLE IF EXISTS `profil_action`;
CREATE TABLE `profil_action` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ref_profil` int(10) NOT NULL,
  `ref_action` int(10) NOT NULL,
  `active` tinyint(1) DEFAULT '0',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_refprofil_refaction` (`ref_profil`,`ref_action`)
) ENGINE=InnoDB AUTO_INCREMENT=11064 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for profil_application
-- ----------------------------
DROP TABLE IF EXISTS `profil_application`;
CREATE TABLE `profil_application` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ref_profil` int(10) NOT NULL,
  `ref_application` int(10) NOT NULL,
  `active` tinyint(1) DEFAULT '0',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_refprofil_refapplication` (`ref_profil`,`ref_application`,`active`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3252 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for profil_info
-- ----------------------------
DROP TABLE IF EXISTS `profil_info`;
CREATE TABLE `profil_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ref_profil` int(10) NOT NULL,
  `ref_info` int(10) NOT NULL,
  `active` tinyint(1) DEFAULT '0',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `partially_visible` tinyint(1) NOT NULL DEFAULT '0',
  `star_number` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_refprofil_ref_info` (`ref_profil`,`ref_info`)
) ENGINE=InnoDB AUTO_INCREMENT=112208 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for rubric
-- ----------------------------
DROP TABLE IF EXISTS `rubric`;
CREATE TABLE `rubric` (
  `id_rubric` int(10) NOT NULL AUTO_INCREMENT,
  `label_rubric` varchar(255) NOT NULL,
  `tag_rubric` varchar(255) NOT NULL,
  `ref_module` int(10) NOT NULL,
  `ref_level` int(10) NOT NULL,
  `priority` int(10) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_rubric`),
  UNIQUE KEY `tag_rubric` (`tag_rubric`),
  KEY `idx_id_reflevel_ref_module` (`id_rubric`,`ref_module`,`ref_level`)
) ENGINE=InnoDB AUTO_INCREMENT=760 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id_user` int(10) NOT NULL AUTO_INCREMENT,
  `fullname` varchar(255) NOT NULL,
  `ref_profil` int(10) NOT NULL,
  `username` varchar(255) NOT NULL,
  `active` tinyint(1) DEFAULT '1',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `access_sup` tinyint(1) DEFAULT '0',
  `ref_groupe` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_user`),
  KEY `idx_profls_user` (`ref_profil`,`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6232 DEFAULT CHARSET=latin1;
