package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseUser;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2023-06-03  09:37
 */
public interface HouseUserMapper extends BaseMapper<HouseUser> {
    void deleteByHouseId(Integer houseId);

    /**
     * 根据houseId查询房东列表
     * @param houseId
     * @return
     */
    List<HouseUser> findHouseUserByHouseId(Integer houseId);
}
