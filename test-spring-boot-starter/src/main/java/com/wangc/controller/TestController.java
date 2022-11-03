package com.wangc.controller;

import com.wangc.log.annotation.MyLog;
import com.wangc.service.AService;
import com.wangc.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 * @Description:
 * @date 2022/10/28 15:33
 */
@RestController
public class TestController {

    @Autowired
    private HelloService helloService;

    @Autowired
    private AService aService;

    @RequestMapping("/test/testStarter")
    @MyLog(desc = "testStarter")
    public String testStarter() {
        aService.say();
        return helloService.sayHello();
    }
}
