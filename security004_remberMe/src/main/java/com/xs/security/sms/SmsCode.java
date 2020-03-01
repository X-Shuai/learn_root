package com.xs.security.sms;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: security_root
 * @description: 短信登录
 * @author: xs-shuai.com
 * @create: 2020-02-29 20:42
 **/
@Data
public class SmsCode {
    private String code;
    private LocalDateTime expireTime;

    public SmsCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public SmsCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    /***
     * 过期
     * @return
     */
    public boolean isExpire() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
