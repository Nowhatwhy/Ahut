package org.nowhatwhy.ahut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nowhatwhy.ahut.constant.ErrorCode;
import org.nowhatwhy.ahut.constant.RedisConstant;
import org.nowhatwhy.ahut.dto.BindingDormDTO;
import org.nowhatwhy.ahut.entity.Building;
import org.nowhatwhy.ahut.entity.Dorm;
import org.nowhatwhy.ahut.entity.UserBinding;
import org.nowhatwhy.ahut.exception.BusinessException;
import org.nowhatwhy.ahut.mapper.UserBindingMapper;
import org.nowhatwhy.ahut.service.IBuildingService;
import org.nowhatwhy.ahut.service.IDormService;
import org.nowhatwhy.ahut.service.IUserBindingService;
import org.nowhatwhy.ahut.utils.UserHolder;
import org.nowhatwhy.ahut.vo.UserBindingVO;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserBindingServiceImpl extends ServiceImpl<UserBindingMapper, UserBinding> implements IUserBindingService {
    private final IDormService dormService;
    private final IBuildingService buildingService;
    private final RedissonClient redissonClient;
    private final UserBindingMapper userBindingMapper;
    public void saveBinding(BindingDormDTO bindingDormDTO) {
        Long userId = UserHolder.get().getId();
        // 1.检查buildingPk是否存在
        Long buildingPk = bindingDormDTO.getBuildingPk();
        if (!buildingService.lambdaQuery().eq(Building::getId, buildingPk).exists()) {
            log.warn("无效的宿舍楼");
            throw new BusinessException(ErrorCode.BUILDING_INFO_ERROR, "无效的宿舍楼");
        }

        // 2.检查dorm是否存在
        Dorm dorm = Dorm.builder()
                .buildingPk(buildingPk)
                .roomNo(bindingDormDTO.getRoomNo())
                .electricityType(bindingDormDTO.getElectricityType())
                .build();

        try {
            dormService.save(dorm);
            log.info("宿舍不存在，新建宿舍：{}", dorm);
        }catch (DuplicateKeyException e) {
            log.info("宿舍已存在");
            dorm = dormService.lambdaQuery()
                    .eq(Dorm::getBuildingPk, buildingPk)
                    .eq(Dorm::getRoomNo, bindingDormDTO.getRoomNo())
                    .eq(Dorm::getElectricityType, bindingDormDTO.getElectricityType())
                    .one();
        }

        UserBinding userBinding = UserBinding.builder()
                .dormId(dorm.getId())
                .userId(userId)
                .build();
        // 3.检查绑定关系是否超过5个或者重复
        RLock lock = redissonClient.getLock(RedisConstant.USER_BINDING_KEY + userId);
        boolean locked = lock.tryLock();
        if (!locked) {
            log.warn("请勿重复提交");
            throw new BusinessException("请勿频繁提交");
        }
        try {
            if (lambdaQuery()
                    .eq(UserBinding::getUserId, userId)
                    .eq(UserBinding::getDormId, dorm.getId())
                    .exists()) {
                throw new BusinessException(ErrorCode.DORM_EXIST, "宿舍已绑定");
            }

            if (lambdaQuery().eq(UserBinding::getUserId, userId).count() >= 5) {
                throw new BusinessException("最多绑定5个宿舍");
            }

            save(userBinding);
            log.info("用户绑定成功");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return;
    }

    @Override
    public List<UserBindingVO> listBindings() {
        return userBindingMapper.listUserBindings(UserHolder.get().getId());
    }
}
