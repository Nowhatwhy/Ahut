CREATE TABLE dorm (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '宿舍ID',

                      building_pk BIGINT NOT NULL COMMENT '楼栋主键（关联 building.id）',
                      room_no VARCHAR(32) NOT NULL COMMENT '房间号',
                      electricity_type VARCHAR(32) NOT NULL COMMENT '电费类型',

                      create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                      update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                      UNIQUE KEY uk_dorm (building_pk, room_no, electricity_type),
                      INDEX idx_building (building_pk)
) COMMENT='宿舍表';