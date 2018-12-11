ALTER TABLE eth_trade_record
  CHANGE `flow_type` `from_flow_type` TINYINT(2) DEFAULT 1  NOT NULL  COMMENT '流水类型,1:lockedbalance,2:balance,3积分',
  CHANGE `trade_no` `trade_no` VARCHAR(64) CHARSET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT ''  NOT NULL  COMMENT '交易订单号',
  ADD COLUMN `to_flow_type` TINYINT(2) DEFAULT 1  NOT NULL  COMMENT '流水类型,1:lockedbalance,2:balance,3积分' AFTER `trade_no`;

ALTER TABLE eth_asset_flow
  CHANGE `trade_no` `trade_no` VARCHAR(64) CHARSET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL  COMMENT '交易号';

ALTER TABLE eth_asset_flow  CHANGE `trade_type` `trade_type` INT(11) NOT NULL  COMMENT '流水类型_flow_type';
ALTER TABLE eth_asset_flow  ADD column `opt_type`  INT(11) NOT NULL  COMMENT '0:系统兑换，1：用户兑换  ';

ALTER TABLE eth_trade_record
  CHANGE `remark` `from_remark` VARCHAR(1024) CHARSET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT ''  NULL  COMMENT 'from交易备注',
  ADD COLUMN `to_remark` VARCHAR(1024) DEFAULT ''  NULL  COMMENT 'to交易备注' AFTER `to_flow_type`;