package com.wangc.shardingsphere.shardingjdbc.service;

import com.wangc.shardingsphere.shardingjdbc.entity.User;

import java.util.List;

/**
 *
 */
public interface UserService {

    /**
     * 新增用户
     * @param user
     */
    void addUser(User user);

    /**
     * 查询所有用户
     * @return
     */
    List<User> findUsers();

    /**
     * 从主库数据源中查询所有用户
     * @return
     */
    List<User> findUsersByMasterDataSource();

    /**
     * 该方法实现包含对数据库的读写操作，通过日志观察读写操作是分别用的读写主从数据源还是用的一个数据源
     *
     * 经过验证得出结论：
     * 如果涉及到读写两个库的操作，是会用对应的数据源的，比如这个方法里面的
     * userMapper.update 用的是master数据源
     * userMapper.selectObjs 用的是slave数据源
     */
    void shardingDataSource();

    /**
     * 结合aop来切换数据源
     */
    void changeDataSourceWithAop();


    /**
     * 结合aop来实现默认从库查询为空切换主库再次调用
     */
    User changeDataSourceWithAopReinvoke();
}
