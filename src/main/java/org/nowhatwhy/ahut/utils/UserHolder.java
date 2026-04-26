package org.nowhatwhy.ahut.utils;

import org.nowhatwhy.ahut.enitity.User;

public class UserHolder {
    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();
    public static void save(User user) {
        userThreadLocal.set(user);
    }
    public static User get() {
        return userThreadLocal.get();
    }
    public static void remove() {
        userThreadLocal.remove();
    }
}
