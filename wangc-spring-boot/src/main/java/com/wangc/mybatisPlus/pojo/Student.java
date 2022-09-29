package com.wangc.mybatisPlus.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author
 * @Description:
 * @date 2022/9/27 19:04
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
//@TableName("student")
public class Student {

    /**
     * @TableId 用于标记主键字段，一个实体类里面只能有一个字段被标记，IdType.AUTO表示自增长的主键生成策略
     * type = IdType.ASSIGN_ID 雪花算法生成的主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

//    @TableField("name")
    private String name;

//    @TableField("age")
    private Integer age;

    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
