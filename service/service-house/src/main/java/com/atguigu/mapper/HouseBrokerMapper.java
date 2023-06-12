package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseBroker;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2023-06-03  09:37
 */
public interface HouseBrokerMapper extends BaseMapper<HouseBroker> {
    void deleteByHouseId(Integer houseId);

    /**
     * 根据房屋id查询经纪人列表
     * @param houseId
     * @return
     */
    List<HouseBroker> findHouseBrokerByHouseId(Integer houseId);
}
