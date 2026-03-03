package com.example.minimall.util;

public class ValidationUtil {

    // 用户名：4-16位，只能字母数字
    public static String validateUsername(String username) {
        if (username == null || !username.matches("^[a-zA-Z0-9]{4,16}$")) {
            return "用户名必须为4-16位，只能包含字母和数字";
        }
        return null;
    }

    // 密码规则：
    // 1. 至少8位
    // 2. 包含大写、小写、数字、特殊字符
    // 3. 不能与用户名相同
    public static String validatePassword(String username, String password) {
        if (password == null || password.length() < 8) {
            return "密码长度至少8位";
        }
        if (password.equals(username)) {
            return "密码不能与用户名相同";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "密码必须包含至少1个大写字母";
        }
        if (!password.matches(".*[a-z].*")) {
            return "密码必须包含至少1个小写字母";
        }
        if (!password.matches(".*\\d.*")) {
            return "密码必须包含至少1个数字";
        }
        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            return "密码必须包含至少1个特殊字符";
        }
        return null;
    }
}
