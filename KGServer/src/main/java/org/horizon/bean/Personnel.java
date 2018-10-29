package org.horizon.bean;

import java.util.Date;

/**
 * 班级信息
 * @author lzd
 */
public class Personnel {
    private Long id;
    private String name;
    private Long num_stu;
    private String des;
    private Date begin_date;
    private Date end_date;
    private String beginDate;
    private String endDate;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNum_stu() {
        return num_stu;
    }

    public void setNum_stu(Long num_stu) {
        this.num_stu = num_stu;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Date getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(Date begin_date) {
        this.begin_date = begin_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}