package com.wangc.base.aop.controller;

import com.wangc.model.RestResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @Description:
 * @date 2022/10/8 20:06
 */
@RestController
@RequestMapping("/aop")
public class AopController {

    @RequestMapping("/test")
    public RestResult<Map<String, Object>> test() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", "wangc");
        return RestResult.ok(result);
    }
}
