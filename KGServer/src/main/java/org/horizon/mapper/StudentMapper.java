package org.horizon.mapper;

import org.apache.ibatis.annotations.Param;
import org.horizon.bean.Student;

import java.util.List;

/**
 * Created by thinkpad on 2018/2/24.
 */
public interface StudentMapper {
    List<Student> getAllStudents(@Param("start") int start, @Param("size") int size, @Param("keywords") String keywords);

    List<Student> getAllClass();

    int addStudent(Student student);

    int updateStudent(Student student);

    int deleteStudentById(@Param("ids") String[] ids);

    Long getCount(@Param("keywords") String keywords);

    List<Student> getStusByPage(Object o, Object o1, String s, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9);

    int addStudents(@Param("students") List<Student> students);
}
