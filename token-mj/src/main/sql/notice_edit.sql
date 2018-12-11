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

 Date: 03/10/2018 13:09:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for notice_edit
-- ----------------------------
DROP TABLE IF EXISTS `notice_edit`;
CREATE TABLE `notice_edit`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '编号',
  `notice_title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公告标题',
  `notice_context` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '公告内容',
  `consignee_num` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '收件人编号',
  `statue` int(2) NOT NULL DEFAULT 1 COMMENT '公告状态 1-新增，2-修改，3-删除，9-生效',
  `is_pop` int(2) NOT NULL DEFAULT 2 COMMENT '是否弹窗(默认否) 1-是，2-否',
  `msg_type` int(2) NOT NULL COMMENT '消息类型 1-系统消息，2-个人消息',
  `create_date` datetime(0) NOT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

--2018-10-17 添加字段
ALTER TABLE notice_edit ADD pop_pic_url varchar(256) COMMENT '弹屏图片url';

--2018-10-20 添加字段
ALTER TABLE notice_edit ADD accessory_url varchar(256) COMMENT '附件url';
ALTER TABLE notice_edit ADD file_name varchar(256) COMMENT '附件名称';
--ALTER TABLE notice_edit ADD accessory_url2 varchar(256) COMMENT '附件url2';
--ALTER TABLE notice_edit ADD accessory_url3 varchar(256) COMMENT '附件url3';

