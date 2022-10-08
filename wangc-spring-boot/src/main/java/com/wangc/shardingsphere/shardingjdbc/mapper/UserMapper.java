package com.wangc.shardingsphere.shardingjdbc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangc.shardingsphere.shardingjdbc.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 * @Description:
 * @date 2022/10/8 15:42
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    @Insert("insert into tb_user(nickname, password, sex, birthday) values(#{nickname}, #{password}, #{sex}, #{birthday})")
    void addUser(User user);

    @Select("select * from tb_user")
    List<User> findUsers();
}
