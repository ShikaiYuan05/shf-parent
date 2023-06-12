package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Admin;

import java.util.List;

public interface AdminService extends BaseService<Admin> {
    void assignRole(Integer adminId, List<Integer> roleIds);

    Admin findByUsername(String username);
}