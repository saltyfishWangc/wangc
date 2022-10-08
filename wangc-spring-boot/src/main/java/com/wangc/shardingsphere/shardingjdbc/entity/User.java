package com.wangc.shardingsphere.shardingjdbc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author
 * @Description:
 * @date 2022/10/8 15:41
 */
@Data
@TableName("tb_user")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String nickname;

    private String password;

    private Integer sex;

    private String birthday;
}
