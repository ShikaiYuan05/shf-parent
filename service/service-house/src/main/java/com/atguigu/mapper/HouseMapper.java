package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.House;
import com.atguigu.entity.vo.HouseQueryVo;
import com.atguigu.entity.vo.HouseVo;

import java.util.List;

public interface HouseMapper extends BaseMapper<House> {
    Integer findHouseCountByCommunityId(Integer communityId);

    List<HouseVo> findFrontPageList(HouseQueryVo houseQueryVo);
}

