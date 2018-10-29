package org.horizon.bean;

/**
 * Created by thinkpad on 2018/2/24.
 */
public class Student {
    private int id;
    private String name;
    private String studentId;
    private String gender;
    private int studentClassId;
    private String studentClassName;
    private double ordinary_performance;
    private double among_them;
    private double final_exam;
    private double total_achievement;
    private String formula;
    private int create_by;
    private String create_date;
    private int update_by;
    private String update_date;
    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getStudentClassId() {
        return studentClassId;
    }

    public void setStudentClassId(int studentClassId) {
        this.studentClassId = studentClassId;
    }

    public String getStudentClassName() {
        return studentClassName;
    }

    public void setStudentClassName(String studentClassName) {
        this.studentClassName = studentClassName;
    }

    public int getCreate_by() {
        return create_by;
    }

    public void setCreate_by(int create_by) {
        this.create_by = create_by;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public int getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(int update_by) {
        this.update_by = update_by;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public double getOrdinary_performance() {
        return ordinary_performance;
    }

    public void setOrdinary_performance(double ordinary_performance) {
        this.ordinary_performance = ordinary_performance;
    }

    public double getAmong_them() {
        return among_them;
    }

    public void setAmong_them(double among_them) {
        this.among_them = among_them;
    }

    public double getFinal_exam() {
        return final_exam;
    }

    public void setFinal_exam(double final_exam) {
        this.final_exam = final_exam;
    }

    public double getTotal_achievement() {
        return total_achievement;
    }

    public void setTotal_achievement(double total_achievement) {
        this.total_achievement = total_achievement;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
