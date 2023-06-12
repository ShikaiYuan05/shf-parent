package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Permission;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2023-06-06  14:54
 */
public interface PermissionService extends BaseService<Permission> {
    List<Map<String, Object>> findZNodes(Long roleId);

    List<Permission> findMenu(Integer adminId);

    List<Permission> findPermissionListByAdminId(Integer roleId);
}
