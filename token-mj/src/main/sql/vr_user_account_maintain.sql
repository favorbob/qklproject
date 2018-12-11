CREATE TABLE vr_user_account_maintain  (
	id varchar(64) NOT NULL COMMENT '编号',
	user_id   varchar(36)  NOT NULL COMMENT '用户ID',
	create_date datetime NOT NULL COMMENT '创建时间',
	ac_type tinyint(2)   NOT NULL COMMENT '类别(1:资产,2:MJ, 3:AIIC)',
	before_modife varchar(12)  COMMENT '资产修改前',
	after_modife  varchar(12)  COMMENT '资产修改后',
	remark varchar(128)   COMMENT '备注',
	PRIMARY KEY (id) USING BTREE
);
