package com.wangc.mybatisPlus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangc.mybatisPlus.mapper.StudentMapper;
import com.wangc.mybatisPlus.pojo.Student;
import com.wangc.mybatisPlus.pojo.param.StudentQueryParam;
import com.wangc.mybatisPlus.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * @Description:
 * @date 2022/9/27 19:15
 */
@Service("studentService")
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentMapper studentMapper;

    @Override
    public int saveStudent(Student student) {
        // insert方法是StudentMapper继承自BaseMapper的方法
        studentMapper.insert(student);
        /**
         * 配合Student实体类id被@TableId(value = "id", IdType.AUTO)标记为自增长的主键策略，
         * 那么这里insert完成后会将生成的主键赋值给student，所以就可以拿到插入记录的主键值
         *
         * 注意：直接return studentMapper.insert(student); 是不能达到返回主键值的效果的，因为
         * 按照上面所说，主键值被赋值在了student对象的id属性了，并没有通过insert方法直接返回
         *
         */
        return student.getId();
    }

    @Override
    public Student findStudentById(int id) {
        // QueryWrapper是用方法来包装查询参数
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        // selectOne方法是StudentMapper继承自BaseMapper的方法
        return studentMapper.selectOne(wrapper);
    }

    @Override
    public List<Student> findAllStudent() {
        // selectList方法是StudentMapper继承自BaseMapper的方法
        return studentMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public int deleteStudentById(int id) {
        // deleteById方法是StudentMapper继承自BaseMapper的方法
        return studentMapper.deleteById(id);
    }

    @Override
    public int updateStudentById(Student student) {
        // updateById方法是StudentMapper继承自BaseMapper的方法
        return studentMapper.updateById(student);
    }

    @Override
    public List<Student> findStudentByFirstName(String firstName) {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        // likeRight表示 name%
        // likeLeft 表示 %name
        wrapper.likeRight("name", firstName);
        return studentMapper.selectList(wrapper);
    }

    @Override
    public List<Student> queryStudentListWithMapperXml(StudentQueryParam param) {
        // queryStudentListWithMapperXml方法是StudentMapper自定义的一个方法，对应的脚本
        return studentMapper.queryStudentListWithMapperXml(param);
    }

    @Override
    public Student queryStudentWithAnnotation(StudentQueryParam param) {
        return studentMapper.queryStudentWithAnnotation(param);
    }
}
