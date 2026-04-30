package org.nowhatwhy.ahut.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nowhatwhy.ahut.constant.ErrorCode;
import org.nowhatwhy.ahut.constant.RedisConstant;
import org.nowhatwhy.ahut.dto.BindingDormDTO;
import org.nowhatwhy.ahut.dto.UserDTO;
import org.nowhatwhy.ahut.dto.UserTokenDTO;
import org.nowhatwhy.ahut.dto.UserUpdateDTO;
import org.nowhatwhy.ahut.entity.User;
import org.nowhatwhy.ahut.exception.BusinessException;
import org.nowhatwhy.ahut.mapper.UserMapper;
import org.nowhatwhy.ahut.service.IUserService;
import org.nowhatwhy.ahut.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private final StringRedisTemplate stringRedisTemplate;
    private final UserBindingServiceImpl userBindingService;
    @Override
    public String login(UserDTO userDTO) {
        log.info("开始验证验证码，用户信息: {}", userDTO);
        String key = RedisConstant.CAPTCHA_CODE_KEY + userDTO.getQq();
        String code = stringRedisTemplate.opsForValue().get(key);

        if (code == null || !code.equals(userDTO.getQqCaptcha())) {
            throw new BusinessException(ErrorCode.CAPTCHA_ERROR, "验证码错误");
        }

        log.info("验证码正确，开始注册或登录");
        if (lambdaQuery().eq(User::getQq, userDTO.getQq()).oneOpt().isEmpty()) {
            log.info("用户不存在，创建用户");
            String password = userDTO.getPassword();

            if(!StringUtils.hasText(password) || password.length() < 6) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "密码长度不能小于6位");
            }

            User newUser = BeanUtil.copyProperties(userDTO, User.class);
            newUser.setUsername(userDTO.getQq());
            save(newUser);
        }
        User user = lambdaQuery().eq(User::getQq, userDTO.getQq()).eq(User::getPassword, userDTO.getPassword()).one();
        // 如果密码错误
        if (user == null) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR, "密码错误");
        }
        log.info("用户信息: {}", user);
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        BeanUtil.copyProperties(user, userTokenDTO);
        Map<String, Object> userMap = BeanUtil.beanToMap(userTokenDTO, new HashMap<>(), CopyOptions.create()
                .setIgnoreNullValue(true)
                .setFieldValueEditor((filedKey, filedValue) -> filedValue.toString()));
        String token = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash().putAll(RedisConstant.LOGIN_TOKEN + token, userMap);
        stringRedisTemplate.expire(RedisConstant.LOGIN_TOKEN + token, RedisConstant.LOGIN_TOKEN_TTL, TimeUnit.SECONDS);
        log.info("用户登录成功，token: {}", token);
        stringRedisTemplate.delete(key);
        return token;
    }

    @Override
    public void sendCode(String qq) {
        if (!StringUtils.hasText(qq)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "qq不能为空");
        }

        log.info("开始发送验证码");
        // 生成验证码
        String code = ThreadLocalRandom.current().nextInt(100000, 999999) + "";
        stringRedisTemplate.opsForValue().set(RedisConstant.CAPTCHA_CODE_KEY + qq, code, RedisConstant.CAPTCHA_CODE_TTL, TimeUnit.SECONDS);
        log.info("qq: {}，验证码: {}", qq, code);
        // TODO 请求qq bot后端发送验证码
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        User user = BeanUtil.copyProperties(userUpdateDTO, User.class);
        user.setId(UserHolder.get().getId());
        lambdaUpdate().update(user);
        log.info("用户: {} 信息更新成功", UserHolder.get());
    }

    @Override
    public void bindDorm(BindingDormDTO bindingDormDTO) {
        Long userId = UserHolder.get().getId();
        userBindingService.bind(userId, bindingDormDTO);
    }
}
