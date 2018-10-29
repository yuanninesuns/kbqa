package org.horizon.controller;

import org.horizon.bean.Personnel;
import org.horizon.bean.RespBean;
import org.horizon.common.poi.PersonnelUtils;
import org.horizon.service.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by sang on 2017/12/29.
 */
@RestController
@RequestMapping("/api/personnel/basic")
public class PersonnelController {

    @Autowired
    PersonnelService personnelService;


    @RequestMapping(value = "/basicdata", method = RequestMethod.GET)
    public Map<String, Object> getAllInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("personnel", personnelService.getAllInfo());
        return map;
    }

    /**
     * 删除
     * @param ids id
     * @return RespBean
     */
    @RequestMapping(value = "/delPersonnel/{ids}", method = RequestMethod.DELETE)
    public RespBean deletePersonnelById(@PathVariable String ids) {
        if (personnelService.deletePersonnelById(ids)) {
            return new RespBean("success", "删除成功!");
        }
        return new RespBean("error", "删除失败!");
    }

    /**
     * 刷新页面
     * @param page 页面
     * @param size 大小
     * @param keywords 关键字
     * @return  Map<String, Object>
     */
    @RequestMapping(value = "/personnel", method = RequestMethod.GET)
    public Map<String, Object> getPersonnelByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "") String keywords) {
        Map<String, Object> map = new HashMap<>();
        List<Personnel> personnelByPage = personnelService.getPersonnelByPages(page, size, keywords);
        Long count = personnelService.getCountByKeywords(keywords);
        map.put("personnels", personnelByPage);
        map.put("count", count);
        return map;
    }

    /**
     * 导出文件
     * @return
     */
    @RequestMapping(value = "/exportPersonnel", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportPersonnel() {
        return PersonnelUtils.exportEmp2Excel(personnelService.getAllPersonnelloyees());
    }

//    /**
//     * 导入文件
//     * @param file
//     * @return
//     */
//    @RequestMapping(value = "/importPersonnel", method = RequestMethod.POST)
//    public RespBean importPersonnel(MultipartFile file) {
//        List<Personnel> emps = PoiUtils.importEmp2List(file,personnelService.getAllNations(),empService.getAllPolitics(),departmentService.getAllDeps(),positionService.getAllPos(),jobLevelService.getAllJobLevels());
//        if (personnelService.addEmps(emps) == emps.size()) {
//            return new RespBean("success", "导入成功!");
//        }
//        return new RespBean("error", "导入失败!");
//    }


    /**
     * 添加
     * @param
     * @return
     */
    @RequestMapping(value = "/addPersonnel", method = RequestMethod.POST)
    public RespBean addPersonnel(Personnel personnel) {
        //int aa = personnelService.addPersonnel(personnel);
        if (personnelService.addPersonnel(personnel) == 1) {
            return new RespBean("success", "添加成功!");
        }
        return new RespBean("error", "添加失败!");
    }

    /**
     * 更新
     * @param employee
     * @return
     */
    @RequestMapping(value = "/updatePersonnel", method = RequestMethod.PUT)
    public RespBean updatePersonnel(Personnel personnel) {
        if (personnelService.updatePersonnel(personnel) == 1) {
            return new RespBean("success", "更新成功!");
        }
        return new RespBean("error", "更新失败!");
    }
}
