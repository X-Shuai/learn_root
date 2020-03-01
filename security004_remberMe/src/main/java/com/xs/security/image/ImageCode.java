package com.xs.security.image;

import lombok.Data;

import javax.annotation.sql.DataSourceDefinition;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * @program: security_root
 * @description: image
 * @author: xs-shuai.com
 * @create: 2020-02-27 13:51
 **/
@Data
public class ImageCode {


    private BufferedImage image;

    private String code;

    private LocalDateTime expireTime;

    public ImageCode() {
    }

    public ImageCode(BufferedImage image, String code, int expireIn) {
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        this.image = image;
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
