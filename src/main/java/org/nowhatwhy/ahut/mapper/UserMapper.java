package org.nowhatwhy.ahut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.nowhatwhy.ahut.enitity.User;


@Mapper
public interface UserMapper extends BaseMapper<User> {

}
