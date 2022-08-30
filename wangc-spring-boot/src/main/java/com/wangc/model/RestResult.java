package com.wangc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @Description:
 * @date 2022/8/29 17:36
 */
@ApiModel(value = "接口统一返回结果")
@Data
public class RestResult<T> implements Serializable {

    // position属性显式出现在参数中的顺序
    @ApiModelProperty(value = "响应码", position = 1, example = "200")
    private Integer code;

    @ApiModelProperty(value = "返回消息", position = 2, example = "操作成功")
    private String message;

    @ApiModelProperty(value = "操作是否成功", position = 3, example = "true")
    private Boolean success;

    @ApiModelProperty(value = "响应数据", position = 4)
    private T data;

    private RestResult() {
        this.code = ResponseCode.SUCCESS.code;
        this.message = ResponseCode.SUCCESS.message;
        this.success = true;
    }

    private RestResult(Integer code, String message, Boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public RestResult(T data) {
        this();
        this.data = data;
    }

    public static <T> RestResult<T> ok(T data) {
        return new RestResult<T>(data);
    }

    public static RestResult fail(Integer code, String message) {
        return new RestResult(code, message, false);
    }

    public static RestResult fail(ResponseCode responseCode) {
        return new RestResult(responseCode.code, responseCode.message, false);
    }
}
