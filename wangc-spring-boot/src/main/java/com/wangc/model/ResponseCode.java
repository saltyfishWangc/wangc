package com.wangc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(200, "响应成功"),
    FAIL(500, "系统异常");

    int code;
    String message;
}
