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

 Date: 01/10/2018 13:41:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for center_edit_service
-- ---------客服编辑管理 标题和内容-------------------
DROP TABLE IF EXISTS `center_edit_service`;
CREATE TABLE `center_edit_service`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `headline` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `context` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `create_date` datetime(0) NOT NULL COMMENT '创建时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
