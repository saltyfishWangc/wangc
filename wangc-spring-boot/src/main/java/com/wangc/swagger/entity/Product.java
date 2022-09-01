package com.wangc.swagger.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author
 * @Description:
 * @date 2022/8/29 17:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("Product-产品实体类")
public class Product {

    @ApiModelProperty(value = "产品编号", example = "5621")
    private Integer productId;

    @ApiModelProperty(value = "产品名称", example = "千张")
    private String productName;

    @ApiModelProperty(value = "价格", example = "2.5")
    private Double price;
}
