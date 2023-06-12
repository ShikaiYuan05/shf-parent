package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Community;
import com.atguigu.mapper.CommunityMapper;
import com.atguigu.mapper.HouseMapper;
import com.atguigu.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = CommunityService.class)
public class CommunityImpl extends BaseServiceImpl<Community> implements CommunityService {

    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    private HouseMapper houseMapper;

    @Override
    public BaseMapper<Community> getBaseMapper() {
        return communityMapper;
    }


    @Transactional
    public boolean delete(Integer id) {
        if (houseMapper.findHouseCountByCommunityId(id)>0) {
            throw new RuntimeException("nonono");
        }
        return houseMapper.delete(id);

    }


}
