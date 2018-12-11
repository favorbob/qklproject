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

 Date: 01/10/2018 13:42:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for center_edit_service_pic
-- ----------客服编辑管理 客服图片------------------
DROP TABLE IF EXISTS `center_edit_service_pic`;
CREATE TABLE `center_edit_service_pic`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `edit_service_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联客服编辑id',
  `service_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客服名称',
  `pic_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客服图片URL',
  `create_date` datetime(0) NOT NULL COMMENT '创建时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `edit_service_id`(`edit_service_id`) USING BTREE,
  CONSTRAINT `center_edit_service_pic_ibfk_1` FOREIGN KEY (`edit_service_id`) REFERENCES `center_edit_service` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
