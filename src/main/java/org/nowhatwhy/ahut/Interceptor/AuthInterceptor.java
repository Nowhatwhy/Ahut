package org.nowhatwhy.ahut.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.nowhatwhy.ahut.constant.UserConstant;
import org.nowhatwhy.ahut.dto.UserTokenDTO;
import org.nowhatwhy.ahut.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token == null || token.isBlank()) {
            response.setStatus(401);
            response.setContentType("text/plain;charset=utf-8");
            response.getWriter().write("用户未登录");
            log.info("用户未登录");
            return false;
        }
        String key = UserConstant.LOGIN_TOKEN + token;
        if (!stringRedisTemplate.hasKey(key)) {
            response.setStatus(401);
            response.setContentType("text/plain;charset=utf-8");
            response.getWriter().write("用户登录信息过期");
            log.info("用户登录信息过期");
            return false;
        }
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        log.info("用户信息: {}", userMap);
        UserTokenDTO user = new UserTokenDTO();
        user.setId(Long.valueOf(userMap.get("id").toString()));
        user.setQq(userMap.get("qq").toString());
        UserHolder.save(user);
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.remove();
    }
}
