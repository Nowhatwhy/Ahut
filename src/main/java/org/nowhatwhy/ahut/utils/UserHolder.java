package org.nowhatwhy.ahut.utils;

import org.nowhatwhy.ahut.dto.UserTokenDTO;

public class UserHolder {
    private static final ThreadLocal<UserTokenDTO> userThreadLocal = new ThreadLocal<>();
    public static void save(UserTokenDTO user) {
        userThreadLocal.set(user);
    }
    public static UserTokenDTO get() {
        return userThreadLocal.get();
    }
    public static void remove() {
        userThreadLocal.remove();
    }
}
