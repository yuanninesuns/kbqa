package org.horizon.mapper;

import org.apache.ibatis.annotations.Param;
import org.horizon.bean.Personnel;

import java.util.Date;
import java.util.List;

/**
 * Created by lzd on 2018/2/24.
 *
 */
public interface PersonnelMapper {

   List<Personnel> getPersonnelByPage();

    /**
     * 删除
     * @param ids id
     * @return int
     */
   int deletePersonnelById(@Param("ids") String[] ids);

    List<Personnel> getPersonnelByPages(@Param("start") Integer start, @Param("size") Integer size, @Param("keywords") String keywords, @Param("startBeginDate") Date startBeginDate, @Param("endBeginDate") Date endBeginDate);

    Long getCountByKeywords(@Param("keywords") String keywords, @Param("startBeginDate") Date startBeginDate, @Param("endBeginDate") Date endBeginDate);

    /**
     * 导出文件
     * @param start
     * @param size
     * @param keywords
     * @param startBeginDate
     * @param endBeginDate
     * @return
     */
    List<Personnel> getPersonnelloyeeByPage(@Param("start") Integer start, @Param("size") Integer size, @Param("keywords") String keywords, @Param("startBeginDate") Date startBeginDate, @Param("endBeginDate") Date endBeginDate);


    /**
     * 添加
     * @return
     */
    int addPersonnel(Personnel personnel);

    /**
     * 更新
     * @param personnel
     * @return
     */
    int updatePersonnel(@Param("personnel") Personnel personnel);
}
