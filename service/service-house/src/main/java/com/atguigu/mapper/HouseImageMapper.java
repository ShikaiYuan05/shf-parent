package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2023-06-03  09:36
 */
public interface HouseImageMapper extends BaseMapper<HouseImage> {
    void deleteByHouseId(Integer houseId);

    /**
     * 根据房屋id查询房屋图片列表
     * @param houseId
     * @param type
     * @return
     */
    List<HouseImage> findHouseImageByHouseId(@Param("houseId") Integer houseId,@Param("type") Integer type);
}
