package org.nowhatwhy.ahut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.nowhatwhy.ahut.entity.UserBinding;
import org.nowhatwhy.ahut.vo.UserBindingVO;

import java.util.List;

@Mapper
public interface UserBindingMapper extends BaseMapper<UserBinding> {
    List<UserBindingVO> listUserBindings(Long userId);
}
