package com.atguigu.system.custom;

import com.atguigu.common.util.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomMD5PasswordEncoder implements PasswordEncoder {
    //加密方法
    @Override
    public String encode(CharSequence rawPassword) {
        return MD5.encrypt(rawPassword.toString());
    }

    //匹配方法
    @Override
    public boolean matches(CharSequence rawPassword, String encodePass) {
        //将用户的密码通过MD5加密之后与数据库中的密文进行匹配
        return encodePass.equalsIgnoreCase(MD5.encrypt(rawPassword.toString()));
    }
}
