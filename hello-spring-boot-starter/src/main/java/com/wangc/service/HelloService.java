package com.wangc.service;

import lombok.AllArgsConstructor;

/**
 * @author
 * @Description:
 * @date 2022/10/27 19:53
 */
@AllArgsConstructor
public class HelloService {

    private String name;

    private String address;

    public String sayHello() {
        return "你好！我的名字叫 " + name + "，我来自 " + address;
    }
}
