package org.nowhatwhy.ahut.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.nowhatwhy.ahut.dto.BindingDormDTO;
import org.nowhatwhy.ahut.entity.UserBinding;
import org.nowhatwhy.ahut.vo.UserBindingVO;

import java.util.List;

public interface IUserBindingService extends IService<UserBinding> {
    List<UserBindingVO> listBindings();

    void saveBinding(BindingDormDTO bindingDormDTO);

    void deleteBindingsByIds(List<Long> ids);
}
