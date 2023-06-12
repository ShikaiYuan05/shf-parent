package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.House;
import com.atguigu.entity.vo.HouseQueryVo;
import com.atguigu.entity.vo.HouseVo;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface HouseService extends BaseService<House> {
    Map<String, Object> findHouseIndexInfo(Map<String, String> filters);
    Map<String,Object> findInitList();

    PageInfo<HouseVo> findListPage(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo);

    Map<String, Object> findHouseDetail(Integer houseId);
}



