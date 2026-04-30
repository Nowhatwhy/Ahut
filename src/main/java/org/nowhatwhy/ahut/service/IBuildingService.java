package org.nowhatwhy.ahut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.nowhatwhy.ahut.entity.Building;

import java.util.List;

public interface IBuildingService extends IService<Building> {
    List<Building> queryBuildings(String name);
}
