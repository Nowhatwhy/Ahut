package org.nowhatwhy.ahut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nowhatwhy.ahut.entity.Building;
import org.nowhatwhy.ahut.mapper.BuildingMapper;
import org.nowhatwhy.ahut.service.IBuildingService;
import org.springframework.stereotype.Service;

@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements IBuildingService {
}
