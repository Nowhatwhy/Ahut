package org.nowhatwhy.ahut.constant;

public class ErrorCode {
    public static final int SUCCESS = 0;

    public static final int PARAM_ERROR = 1001;

    public static final int NOT_FOUND = 1002;

    public static final int DEFAULT_ERROR = 1003;

    public static final int USER_NOT_EXIST = 2001;

    public static final int USER_ALREADY_EXIST = 2002;

    public static final int CAPTCHA_ERROR = 3001;

    public static final int TOKEN_INVALID = 3002;

    public static final int TOKEN_EXPIRED = 3003;

    public static final int NOT_LOGIN = 3004;

    public static final int PASSWORD_ERROR = 3005;

    public static final int CHARGE_QUERY_FAIL = 4001;

    public static final int BUILDING_INFO_ERROR = 4002;
}

/*
0        → 成功
1xxx     → 通用错误
2xxx     → 用户相关
3xxx     → 认证/登录
4xxx     → 业务数据
5xxx     → 系统错误
 */
