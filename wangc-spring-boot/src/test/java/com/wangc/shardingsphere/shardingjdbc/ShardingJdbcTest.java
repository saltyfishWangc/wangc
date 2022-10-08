package com.wangc.shardingsphere.shardingjdbc;

import com.wangc.shardingsphere.shardingjdbc.entity.User;
import com.wangc.shardingsphere.shardingjdbc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author
 * @Description: ShardingSphere的sharding-jdbc测试类
 * @date 2022/10/8 16:19
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ShardingJdbcTestApplication.class})
public class ShardingJdbcTest {

    @Autowired
    UserService userService;

    @Test
    public void testSaveUser() {
        log.info("测试ShardingJdbc读写分离，注意观察日志操作的对应数据源是否一致");
        User user = new User();
        user.setNickname("wangc");
        user.setPassword("123456");
        user.setSex(0);
        user.setBirthday("1994-12-17");
        userService.addUser(user);
    }

    @Test
    public void testFindUsersByMasterDataSource() {
        log.info("测试ShardingJdbc代码切换数据源，注意观察日志操作的对应数据源是否一致");
        log.info(String.valueOf(userService.findUsersByMasterDataSource()));
    }

    @Test
    public void testFindUsers() {
        log.info("测试ShardingJdbc读写分离，注意观察日志操作的对应数据源是否一致");
        log.info(String.valueOf(userService.findUsers()));
    }
//
//    @Test
//    public void testFindUsersByMasterDataSource() {
//        log.info("测试ShardingJdbc代码切换数据源，注意观察日志操作的对应数据源是否一致");
//        log.info(String.valueOf(userService.findUsersByMasterDataSource()));
//    }

    @Test
    public void testShardingDataSource() {
        log.info("测试ShardingJdbc代码切换数据源，注意观察日志操作的对应数据源是否一致");
        userService.shardingDataSource();
    }

    @Test
    public void testChangeDataSourceWithAop() {
        log.info("测试ShardingJdbc结合aop切换数据源，注意观察日志操作的对应数据源是否一致");
        userService.changeDataSourceWithAop();
    }
}
