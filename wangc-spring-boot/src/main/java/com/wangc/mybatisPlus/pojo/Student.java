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
/**
 * @TableName 注解说明：如果没有这个，那BaseMapper中的方法以及用Mybatis的接口开发，如QueryWrapper这些传参是会报错的，
 * 因为那里面的脚本都是mybatis自己生成的，并不是开发编写的，没有指定表名，需要根据这里的注解得到表名，当然，
 * 如果这里没有注解，那就用类名作为默认表名，所以，对于表名和实体类名不一致的需要加这个注解指明
 */
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
