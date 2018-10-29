package org.horizon.service;

import org.horizon.bean.Personnel;
import org.horizon.mapper.PersonnelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lzd on 2018/2/24.
 *
 */
@Service
@Transactional
public class PersonnelService {

    @Autowired
    PersonnelMapper personnelMapper;
    SimpleDateFormat birthdayFormat = new SimpleDateFormat("yyyy-MM-dd");
    public List<Personnel> getAllInfo() {
        return personnelMapper.getPersonnelByPage();
    }

    /**
     *  删除
     * @param ids id
     * @return boolean
     */
    public boolean deletePersonnelById(String ids) {
        String[] split = ids.split(",");
        return personnelMapper.deletePersonnelById(split) == split.length;
    }


    public List<Personnel> getPersonnelByPages(Integer page, Integer size, String keywords) {
        int start = (page - 1) * size;
        Date startBeginDate = null;
        Date endBeginDate = null;
        return personnelMapper.getPersonnelByPages(start, size, keywords, startBeginDate, endBeginDate);
    }

    public Long getCountByKeywords(String keywords) {
        Date startBeginDate = null;
        Date endBeginDate = null;
        return personnelMapper.getCountByKeywords(keywords, startBeginDate, endBeginDate);
    }

    /**
     * 导出文件
     * @return
     */
    public List<Personnel> getAllPersonnelloyees() {
        return personnelMapper.getPersonnelloyeeByPage(null, null, "", null, null);
    }


    /**
     * 添加
     * @param personnel
     * @return
     */
    public int addPersonnel(Personnel personnel) {
        Date beginContract = personnel.getBegin_date();
        Date endContract = personnel.getEnd_date();
        String beginStr = new SimpleDateFormat("yyyy-MM-dd").format(beginContract);
        String endStr = new SimpleDateFormat("yyyy-MM-dd").format(endContract);
        personnel.setBeginDate(beginStr);
        personnel.setEndDate(endStr);
        return personnelMapper.addPersonnel(personnel);
    }

    /**
     * 更新
     * @param personnel
     * @return
     */
    public int updatePersonnel(Personnel personnel) {
        Date beginContract = personnel.getBegin_date();
        Date endContract = personnel.getEnd_date();
        String beginStr = new SimpleDateFormat("yyyy-MM-dd").format(beginContract);
        String endStr = new SimpleDateFormat("yyyy-MM-dd").format(endContract);
        personnel.setBeginDate(beginStr);
        personnel.setEndDate(endStr);
        return personnelMapper.updatePersonnel(personnel);
    }
}
