/*
 Navicat Premium Data Transfer

 Source Server         : 47.75.238.217
 Source Server Type    : MySQL
 Source Server Version : 50616
 Source Host           : 47.75.238.217:3306
 Source Schema         : mj_dev

 Target Server Type    : MySQL
 Target Server Version : 50616
 File Encoding         : 65001

 Date: 03/10/2018 13:09:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for notice_edit_user
-- ----------------------------
DROP TABLE IF EXISTS `notice_edit_user`;
CREATE TABLE `notice_edit_user`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '编号',
  `notice_consignee` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '收件人编号',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '会员编号',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '会员名称',
  `statue` int(2) NOT NULL DEFAULT 1 COMMENT '是否有效 1-有效，0-无效',
  `is_read` int(2) NOT NULL DEFAULT 1 COMMENT '是否已读 1-未读，2-已读',
  `create_date` datetime(0) NOT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
