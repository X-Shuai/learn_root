package com.xs.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-10-30 15:38
 **/
@ToString
@Accessors(chain = true)
@AllArgsConstructor
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int code = CommonConstants.SUCCESS;

    @Getter
    @Setter
    private HttpStatus httpStatus;


    @Getter
    @Setter
    private T data;


    private String[] messages = {};


    public R() {
        super();
    }

    public R(T data) {
        super();
        this.data = data;
    }

    public R(String... msg) {
        super();
        this.messages = msg;
    }

    public R(T data, String... msg) {
        super();
        this.data = data;
        this.messages = msg;
    }

    public R(T data, int code, String... msg) {
        super();
        this.data = data;
        this.code = code;
        this.messages = msg;
    }

    public R(Throwable e) {
        super();
        setMessage(e.getMessage());
        this.code = CommonConstants.FAIL;
    }

    public static R buildOk(String... messages) {
        return new R(messages);
    }

    public static <T> R buildOkData(T data, String... messages) {
        return new R(data, messages);
    }

    public static <T> R buildFailData(T data, String... messages) {
        return new R(data, CommonConstants.FAIL, messages);
    }

    public static <T> R buildFail(String... messages) {
        return new R(null, CommonConstants.FAIL, messages);
    }

    public static <T> R build(T data, int code, String... messages) {
        return new R(data, code, messages);
    }

    public static <T> R build(int code, String... messages) {
        return new R(null, code, messages);
    }

    public String getMessage() {
        return readMessages();
    }

    public void setMessage(String message) {
        addMessage(message);
    }

    public String readMessages() {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message);
        }
        return sb.toString();
    }

    public void addMessage(String message) {
        this.messages = ObjectUtils.addObjectToArray(messages, message);
    }

}

