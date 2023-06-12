package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleService extends BaseService<Role> {
    Map<String, List<Role>> findRoleListMapByAdminId(Integer id);
    /**
     * 给角色分配权限
     * @param roleId
     * @param permissionIds
     */
    void assignPermission(Integer roleId, List<Integer> permissionIds);

    List<Role> findRoleListByAdminId(Integer adminId);
}
