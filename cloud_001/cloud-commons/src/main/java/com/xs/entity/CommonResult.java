package com.xs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: learn_root
 * @description: 返回工具类
 * @author: xs-shuai.com
 * @create: 2020-04-02 23:16
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

    private Integer code;

    private String message;

    private T data;

    public CommonResult(Integer code, String message) {
        this(code,message,null);
    }
    public static  CommonResult ok(){
        return new CommonResult(200,"请求成功",null);
    }
    public static  CommonResult okData(Object data){
        return new CommonResult(200,"请求成功",data);
    }

    public static  CommonResult fail(Integer code){
        return new CommonResult(code,"请求失败",null);
    }
    public static  CommonResult failMessage(Integer code,String message){
        return new CommonResult(code,message,null);
    }
}
