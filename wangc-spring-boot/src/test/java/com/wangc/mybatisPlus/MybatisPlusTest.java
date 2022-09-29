package com.wangc.mybatisPlus;

import com.wangc.mybatisPlus.mapper.StudentMapper;
import com.wangc.mybatisPlus.pojo.Student;
import com.wangc.mybatisPlus.pojo.param.StudentQueryParam;
import com.wangc.mybatisPlus.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
//import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @Description: MybatisPlus 测试类
 * @date 2022/9/27 19:59
 */
@Slf4j
@RunWith(SpringRunner.class)
/**
 * SpringBoot中的单元测试可以启动服务，可以不启动服务。而在非必要的情况下避免启动整个SpringBoot服务。
 * 如果想要测试跑的更快，就尽量少的实例化不需要的类。
 * 我们这个MybatisPlusTest是专门针对com.wangc.mybatisPlus包做的单元测试，所以我们只需要加载这个包下的类，
 * 其他不相关的类不需要。通过定义一个SpringBoot启动类MybatisPlusTestApplication，里面指定扫描包路径为
 * com.wangc.mybatisPlus.*
 * @SpringBootTest默认是将SpringBoot主类中导入的bean全都包含进来，也就是将SpringBoot主类当作bean的收集器
 *
 */
@SpringBootTest(classes = {MybatisPlusTestApplication.class})
public class MybatisPlusTest {

    @Autowired
    StudentService studentService;

    /**
     * 不能导入spring-boot-starter-test的org.junit.jupiter.api.Test，应该导入org.junit.Test，
     * 否则会报启动测试类时，这个测试方法会被执行，但是最后还是会报initializationError：No runnable methods
     */
    @Test
    public void testSaveStudent() {
        Student student = new Student("王九", 30);
        log.info(String.valueOf(studentService.saveStudent(student)));
    }

    @Test
    public void testFindStudentById() {
        log.info(String.valueOf(studentService.findStudentById(4)));
    }

    @Test
    public void testFindAllStudent() {
        log.info(String.valueOf(studentService.findAllStudent()));
    }

    @Test
    public void testDeleteStudentById() {
        log.info(String.valueOf(studentService.deleteStudentById(1)));
    }

    @Test
    public void testUpdateStudentById() {
        log.info(String.valueOf(studentService.updateStudentById(new Student(2, "李老四", 30))));
    }

    @Test
    public void testFindStudentByFirstName() {
        log.info(String.valueOf(studentService.findStudentByFirstName("李")));
    }

    @Test
    public void testQueryStudentListWithMapperXml() {
        StudentQueryParam param = new StudentQueryParam();
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(5);
        ids.add(9);
        param.setIds(ids);
        log.info(String.valueOf(studentService.queryStudentListWithMapperXml(param)));
    }

    @Test
    public void testQueryStudentWithAnnotation() {
        StudentQueryParam param = new StudentQueryParam();
        param.setId(2);
        log.info(String.valueOf(studentService.queryStudentWithAnnotation(param)));
    }
}
