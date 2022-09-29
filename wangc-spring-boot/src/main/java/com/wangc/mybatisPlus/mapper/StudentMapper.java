package com.wangc.mybatisPlus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangc.mybatisPlus.pojo.Student;
import com.wangc.mybatisPlus.pojo.param.StudentQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StudentMapper extends BaseMapper<Student> {

    /**
     * 自定义一个mapper方法，通过自己写的脚本来查询
     * @param param
     * @return
     */
    List<Student> queryStudentListWithMapperXml(StudentQueryParam param);

    /**
     * 直接通过在接口上加@Select来自定义脚本
     * @param param
     * @return
     */
    @Select("select * from student where id = #{id}")
    Student queryStudentWithAnnotation(StudentQueryParam param);
}
