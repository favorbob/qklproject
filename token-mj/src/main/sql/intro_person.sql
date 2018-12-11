--简介个人已读记录表
CREATE TABLE intro_person  (
  id varchar(64)  NOT NULL COMMENT '编号',
  intro_id varchar(64)  NOT NULL COMMENT '简介ID',
  user_id varchar(36)   NOT NULL COMMENT '用户ID',
  is_read int(2) NOT NULL DEFAULT 2 COMMENT '是否已读 1-未读，2-已读, 默认已读',
  create_date datetime(0) NOT NULL COMMENT '创建时间',
  remarks varchar(255)  DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (id) USING BTREE
) comment='介个人已读记录表';
ALTER TABLE intro_person ADD INDEX index_introid_userid (intro_id,user_id ) ;