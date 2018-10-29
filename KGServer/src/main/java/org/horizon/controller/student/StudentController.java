package org.horizon.controller.student;

import org.horizon.bean.RespBean;
import org.horizon.bean.Student;
import org.horizon.common.poi.StudentUtils;
import org.horizon.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thinkpad on 2018/2/24.
 */
@RestController
@RequestMapping("/student/mana")
public class StudentController {
    @Autowired
    StudentService studentService;

    @RequestMapping("/getAllStudents")
    public Map<String, Object> getAllStudents(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "") String keywords){
        Map<String, Object> map = new HashMap<>();
        List<Student> list = studentService.getAllStudents(page, size, keywords);
        Long count = studentService.getCount(keywords);
        map.put("students", list);
        map.put("count", count);
        return map;
    }
    @RequestMapping("/getAllClass")
    public Map<String, Object> getAllClass(){
        Map<String, Object> map = new HashMap<>();
        List<Student> list = studentService.getAllClass();
        map.put("class", list);
        return map;
    }
    @RequestMapping("/addStudent")
    public RespBean addStudent(Student student){
        if(studentService.addStudent(student) == 1){
            return new RespBean("success", "添加成功!");
        }
        return new RespBean("success", "添加失败!");
    }
    @RequestMapping("/updateStudent")
    public RespBean updateStudent(Student student){
        if(studentService.updateStudent(student) != 0){
            return new RespBean("success", "更新成功!");
        }
        return new RespBean("success", "更新失败!");
    }
    @RequestMapping(value = "/deleteStudentById/{ids}", method = RequestMethod.DELETE)
    public RespBean deleteStudentById(@PathVariable String ids){
        if(studentService.deleteStudentById(ids)){
            return new RespBean("success", "删除成功!");
        }
        return new RespBean("success", "删除失败!");
    }

    @RequestMapping(value = "/exportStudent", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportStudent(){
        return StudentUtils.exportEmp2Excel(studentService.getAllStudents(0,0,null));
    }

    @RequestMapping(value = "/importEmp", method = RequestMethod.POST)
    public RespBean importEmp(MultipartFile file) {
        List<Student> students = StudentUtils.importEmp2List(file);
        if (studentService.addStudents(students) == students.size()) {
            return new RespBean("success", "导入成功!");
        }
        return new RespBean("error", "导入失败!");
    }
}
