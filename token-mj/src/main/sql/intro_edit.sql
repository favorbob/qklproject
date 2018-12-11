/*
 Navicat Premium Data Transfer

 Source Server         : qkl
 Source Server Type    : MySQL
 Source Server Version : 50633
 Source Host           : 47.100.17.11:3306
 Source Schema         : mj_token

 Target Server Type    : MySQL
 Target Server Version : 50633
 File Encoding         : 65001

 Date: 01/10/2018 15:07:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for intro_edit
-- ---------简介编辑管理-------------------
DROP TABLE IF EXISTS `intro_edit`;
CREATE TABLE `intro_edit`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `intro_title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '简介标题',
  `intro_context` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '简介内容',
  `create_date` datetime(0) NOT NULL COMMENT '创建时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

--2018-10-20 添加字段
ALTER TABLE intro_edit ADD accessory_url varchar(256) COMMENT '附件url';
ALTER TABLE intro_edit ADD file_name varchar(256) COMMENT '附件名称';