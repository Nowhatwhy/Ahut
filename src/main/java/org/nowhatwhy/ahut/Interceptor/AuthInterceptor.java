package org.nowhatwhy.ahut.Interceptor;

import cn.hutool.core.bean.BeanUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.nowhatwhy.ahut.constant.RedisConstant;
import org.nowhatwhy.ahut.dto.UserTokenDTO;
import org.nowhatwhy.ahut.entity.User;
import org.nowhatwhy.ahut.service.IUserService;
import org.nowhatwhy.ahut.utils.UserHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Value("${BOT_SECRET}")
    private String botSecret;
    private final StringRedisTemplate stringRedisTemplate;
    private final IUserService userService;
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String ip = getClientIp(request);
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String ua = request.getHeader("User-Agent");
        String botSecret = request.getHeader("bot-secret");
        if (botSecret != null && botSecret.equals(this.botSecret)) {
            log.info("Bot 验证成功");
            String qq = request.getHeader("qq");
            User user = userService.getUserByQQ(qq);
            if (user == null) {
                return false;
            }
            UserHolder.save(BeanUtil.copyProperties(user, UserTokenDTO.class));
            return true;
        }
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            log.warn("未登录请求 IP={} METHOD={} URI={} UA={}", ip, method, uri, ua);
            response.setStatus(401);
            response.setContentType("text/plain;charset=utf-8");
            response.getWriter().write("用户未登录");
            return false;
        }
        token = token.substring(7);
        String key = RedisConstant.LOGIN_TOKEN + token;
        if (!stringRedisTemplate.hasKey(key)) {
            log.warn("Token 过期 IP={} METHOD={} URI={}", ip, method, uri);
            response.setStatus(401);
            response.setContentType("text/plain;charset=utf-8");
            response.getWriter().write("用户登录信息过期");
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
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        UserHolder.remove();
    }

    /**
     * 获取真实 IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
