package org.nowhatwhy.ahut.constant;

public class RedisConstant {
    public static final String CAPTCHA_CODE_KEY = "captcha:qq:";
    public static final int CAPTCHA_CODE_TTL = 5 * 60;
    public static final String LOGIN_TOKEN = "login:token:";
    public static final int LOGIN_TOKEN_TTL = 60 * 60 * 24 * 7;
    public static final String USER_BINDING_KEY = "user:binding:";
}
