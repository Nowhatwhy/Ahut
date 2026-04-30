package org.nowhatwhy.ahut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nowhatwhy.ahut.entity.Dorm;
import org.nowhatwhy.ahut.mapper.DormMapper;
import org.nowhatwhy.ahut.service.IDormService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DormServiceImpl extends ServiceImpl<DormMapper, Dorm> implements IDormService {

}
