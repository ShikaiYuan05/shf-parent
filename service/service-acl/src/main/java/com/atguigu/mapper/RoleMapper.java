package com.atguigu.mapper;
import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Role;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {

    List<Role> findRoleListByAdminId(Integer adminId);
}
