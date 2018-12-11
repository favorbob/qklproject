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

 Date: 01/10/2018 13:42:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for center_home_pic
-- ---------首页图片管理-------------------
DROP TABLE IF EXISTS `center_home_pic`;
CREATE TABLE `center_home_pic`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `pic_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '首页图片url',
  `pic_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '首页图片名称',
  `out_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '超链接',
  `create_date` datetime(0) NOT NULL COMMENT '创建时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE center_home_pic ADD pic_sort int(2) DEFAULT 0 COMMENT '首页排序字段';