package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.HouseBroker;
import com.atguigu.mapper.HouseBrokerMapper;
import com.atguigu.service.HouseBrokerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2023-06-03  10:31
 */
@Service(interfaceClass = HouseBrokerService.class)
public class HouseBrokerServiceImpl extends BaseServiceImpl<HouseBroker> implements HouseBrokerService {
    @Autowired
    private HouseBrokerMapper houseBrokerMapper;
    @Override
    public BaseMapper<HouseBroker> getBaseMapper() {
        return houseBrokerMapper;
    }

    @Override
    public List<HouseBroker> findHouseBrokerByHouseId(Integer houseId) {
        return houseBrokerMapper.findHouseBrokerByHouseId(houseId);
    }
}
