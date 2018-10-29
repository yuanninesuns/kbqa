package org.horizon.service;

import org.horizon.bean.MapJ;
import org.horizon.bean.Student;
import org.horizon.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by thinkpad on 2018/2/24.
 */
@Service
@Transactional
public class StudentService {
    @Autowired
    StudentMapper studentMapper;

    public List<Student> getAllStudents(int page, int size, String keywords){
        int start = (page - 1) * size;
        return studentMapper.getAllStudents(start, size, keywords);
    }

    public List<Student> getAllClass() {
        return studentMapper.getAllClass();
    }

    public int addStudent(Student student) {
        student = this.getDayDate(student);
        List<Student> list = this.getAllStudents(1,1,null);
        if(list.size()>0){
            if(list.get(0).getFormula() != null){
                student.setFormula(list.get(0).getFormula());
                student = this.getAddValue(student);
            }else{
                student.setFormula("未添加");
            }
        }else{
            student.setFormula("未添加");
        }
        return studentMapper.addStudent(student);
    }

    public int updateStudent(Student student) {
        student = this.getDayDate(student);
        if (!("未添加".equals(student.getFormula()))){
            this.getValue(student);
        }
        return studentMapper.updateStudent(student);
    }

    private Student getDayDate(Student student){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        student.setCreate_date(df.format(new Date()));// new Date()为获取当前系统时间
        student.setUpdate_date(df.format(new Date()));
        return student;
    }

    public boolean deleteStudentById(String ids) {
        String[] id = ids.split(",");
        return studentMapper.deleteStudentById(id) == id.length;
    }

    public Long getCount(String keywords){
        return studentMapper.getCount(keywords);
    }

    public List<Student> getAllEmployees() {
        return studentMapper.getStusByPage(null, null, "", null, null, null, null, null, null, null, null);
    }

    public int addStudents(List<Student> students) {
        return studentMapper.addStudents(students);
    }

    public Double getMathValue(List<MapJ> map, String option){
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        double d = 0;
        try {
            for(int i=0; i<map.size();i++){
                MapJ mapj = map.get(i);
                option = option.replaceAll(mapj.getKey(), mapj.getValue());
            }
            Object o = engine.eval(option);
            d = Double.parseDouble(o.toString());
        } catch (ScriptException e) {
            System.out.println("无法识别表达式");
            return null;
        }
        return d;
    }

    public Double getNum(String a, String b, String c, String formula){

        List<MapJ> all = new ArrayList<MapJ>();
        all.add(new MapJ("A",a));
        all.add(new MapJ("B",b));
        all.add(new MapJ("C",c));
        Double value = this.getMathValue(all, formula);
        if(value==null){
            System.out.println("无法计算这个表达式");
        }else{
            return value;
        }
        return 0.0;
    }

    public void getValue(Student student){
        String formula = student.getFormula();
        String a=null;
        String b=null;
        String c=null;
        if(student.getFlag() == 1){
            List<Student> list = this.getAllStudents(0,0,null);
            if(list.size()>0){
                for(int i = 0; i < list.size();i++){
                    a = String.valueOf(list.get(i).getOrdinary_performance());
                    b = String.valueOf(list.get(i).getAmong_them());
                    c = String.valueOf(list.get(i).getFinal_exam());
                    list.get(i).setTotal_achievement(getNum(a,b,c,formula));
                    int num = studentMapper.updateStudent(list.get(i));
                }
            }
        }else{
            a = String.valueOf(student.getOrdinary_performance());
            b = String.valueOf(student.getAmong_them());
            c = String.valueOf(student.getFinal_exam());
            student.setTotal_achievement(getNum(a,b,c,formula));
            int num = studentMapper.updateStudent(student);
        }
    }
    public Student getAddValue(Student student){
        String formula = student.getFormula();
        String a = String.valueOf(student.getOrdinary_performance());
        String b = String.valueOf(student.getAmong_them());
        String c = String.valueOf(student.getFinal_exam());
        student.setTotal_achievement(getNum(a,b,c,formula));
        return student;
    }
}
