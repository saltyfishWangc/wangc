package com.wangc.swagger.controller;

import com.wangc.swagger.entity.Product;
import com.wangc.model.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @Description:
 *  经过在aop功能测试过程中，对于@RequestParam这些参数的校验并不是在执行到方法时校验的，而是在请求做路径映射匹配时就开始校验了。
 *  现象：如果被@RequestParam标记的参数没有传，还没进入到对应的aop通知逻辑就报错了
 * @date 2022/8/29 19:16
 */
@Api("获取产品信息")
@RestController
@RequestMapping("/product")
public class ProductController {

    @PostMapping("/getProductInfo")
    @ApiOperation(value = "根据产品id获取产品信息", notes = "")
    public RestResult<Product> getProductInfo(@RequestParam Integer productId) {
        return RestResult.ok(new Product(productId, "千张", 2.5d));
    }

    @ApiOperation(value = "下单")
    @PostMapping("/order")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", dataTypeClass = Integer.class, paramType = "query", example = "3423432"),
            @ApiImplicitParam(name = "productName", value = "产品名称", dataTypeClass = String.class, paramType = "query", example = "千张"),
            @ApiImplicitParam(name = "price", value = "价格", dataTypeClass = Double.class, paramType = "query", example = "2.4")
    })
    public RestResult<String> order(@ApiIgnore @RequestParam Map<String, String> params) {
        return RestResult.ok(null);
    }

    @PostMapping("/swaggerRetTest")
    @ApiOperation(value = "swagger返回map测试", notes = "")
    public RestResult<Map<String, Object>> swaggerRetTest(@RequestParam Integer productId) {
        Map<String, Object> result = new HashMap<>();
        result.put("name", "wangc");
        return RestResult.ok(result);
    }
}
