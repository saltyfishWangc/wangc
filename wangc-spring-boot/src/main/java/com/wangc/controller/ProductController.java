package com.wangc.controller;

import com.wangc.entity.Product;
import com.wangc.model.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author
 * @Description:
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
}
