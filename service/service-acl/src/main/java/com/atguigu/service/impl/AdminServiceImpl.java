package com.atguigu.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseService;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Admin;
import com.atguigu.mapper.AdminMapper;
import com.atguigu.mapper.AdminRoleMapper;
import com.atguigu.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service(interfaceClass = AdminService.class)
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;


    @Override
    public BaseMapper<Admin> getBaseMapper() {
        return adminMapper;
    }

    @Override
    public void assignRole(Integer adminId, List<Integer> roleIds) {
        //1. 删除当前用户已分配的所有角色
        adminRoleMapper.deleteByAdminId(adminId);
        //2. 批量给用户分配角色
        if (CollectionUtils.isNotEmpty(roleIds)) {
            adminRoleMapper.insertBatch(adminId, roleIds);
        }
    }

    @Override
    public Admin findByUsername(String username) {
        return adminMapper.findByUsername(username);
    }
}


