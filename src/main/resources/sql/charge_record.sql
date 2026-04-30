CREATE TABLE charge_record (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',

                               dorm_id BIGINT NOT NULL COMMENT '宿舍ID',
                               balance DECIMAL(10,2) NOT NULL COMMENT '电费余额',

                               record_date DATE NOT NULL COMMENT '记录日期（用于按天统计）',
                               create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '采集时间',

                               INDEX idx_dorm_time (dorm_id, create_time),
                               INDEX idx_dorm_date (dorm_id, record_date)
) COMMENT='电费记录';