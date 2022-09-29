package com.wangc.mybatisPlus.service;

import com.wangc.mybatisPlus.pojo.Student;
import com.wangc.mybatisPlus.pojo.param.StudentQueryParam;

import java.util.List;

public interface StudentService {

    /**
     * 保存Student
     * @param student
     * @return
     */
    int saveStudent(Student student);

    /**
     * 根据id查找
     * @param id
     * @return
     */
    Student findStudentById(int id);

    /**
     * 查找全部
     * @return
     */
    List<Student> findAllStudent();

    /**
     * 删除Student
     * @param id
     * @return
     */
    int deleteStudentById(int id);

    /**
     * 修改Student
     * @param student
     * @return
     */
    int updateStudentById(Student student);

    /**
     * 根据姓模糊查询
     * @param firstName
     * @return
     */
    List<Student> findStudentByFirstName(String firstName);

    /**
     * 根据mapper.xml自定义的sql查询
     * @param param
     * @return
     */
    List<Student> queryStudentListWithMapperXml(StudentQueryParam param);

    /**
     * 根据@Select自定义的sql查询
     * @param param
     * @return
     */
    Student queryStudentWithAnnotation(StudentQueryParam param);
}
