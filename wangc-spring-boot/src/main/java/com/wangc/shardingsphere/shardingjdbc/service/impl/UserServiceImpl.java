package com.wangc.shardingsphere.shardingjdbc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangc.shardingsphere.shardingjdbc.aop.annotation.MasterDataSource;
import com.wangc.shardingsphere.shardingjdbc.entity.User;
import com.wangc.shardingsphere.shardingjdbc.mapper.UserMapper;
import com.wangc.shardingsphere.shardingjdbc.service.UserService;
import org.apache.shardingsphere.api.hint.HintManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * @Description:
 * @date 2022/10/8 15:48
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public void addUser(User user) {
        userMapper.addUser(user);
    }

    @Override
    public List<User> findUsers() {
        return userMapper.findUsers();
    }

    @Override
    public List<User> findUsersByMasterDataSource() {
        try(HintManager hintManager = HintManager.getInstance()) {
            /**
             * hintManager.setDatabaseShardingValue("master")
             * 直接用这种方式，并不会切换数据源，下面查询还是用的slave数据源
             */
//            hintManager.setDatabaseShardingValue("master");
            hintManager.setMasterRouteOnly();
            userMapper.findUsers();
//            return userMapper.findUsers();
        }
        return userMapper.findUsers();
    }

    @Override
    public void shardingDataSource() {
        User user = new User();
        user.setNickname("我改名了");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nickname", "wangc");
        userMapper.update(user, queryWrapper);

        System.out.println(String.valueOf(userMapper.selectObjs(queryWrapper)));
    }

    @Override
    @MasterDataSource
    public void changeDataSourceWithAop() {
        User user = new User();
        user.setNickname("我改名了");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nickname", "wangc");
        userMapper.update(user, queryWrapper);

        System.out.println(String.valueOf(userMapper.selectObjs(queryWrapper)));
    }
}
