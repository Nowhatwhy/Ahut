package org.nowhatwhy.ahut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.nowhatwhy.ahut.dto.UserDTO;
import org.nowhatwhy.ahut.dto.UserUpdateDTO;
import org.nowhatwhy.ahut.enitity.User;

public interface IUserService extends IService<User> {
    String login(UserDTO userDTO);

    void sendCode(String qq);

    void updateUser(UserUpdateDTO userUpdateDTO);
}
