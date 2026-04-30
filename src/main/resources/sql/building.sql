CREATE TABLE building (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',

                          campus VARCHAR(32) NOT NULL COMMENT '校区（NewS / OldS）',
                          building_id VARCHAR(32) NOT NULL COMMENT '学校返回的楼栋ID',
                          building_name VARCHAR(64) COMMENT '楼栋名称',

                          create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                          UNIQUE KEY uk_building (campus, building_id)
) COMMENT='楼栋表';