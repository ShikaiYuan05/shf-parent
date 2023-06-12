package com.atguigu.mapper;
import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Admin;

public interface AdminMapper extends BaseMapper<Admin>{
    Admin findByUsername(String username);
}