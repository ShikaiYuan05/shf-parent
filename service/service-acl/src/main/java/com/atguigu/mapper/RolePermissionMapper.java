package com.atguigu.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RolePermissionMapper {
    List<Long> findPermissionIdListByRoleId(Long roleId);

    void deleteByRoleId(Integer roleId);

    void insertBatch(@Param("roleId") Integer roleId, @Param("permissionIds") List<Integer> permissionIds);
}
