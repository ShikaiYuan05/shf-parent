package com.atguigu.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminRoleMapper {

    //1.根据adminId查询该用户已分配的角色的id列表
    List<Long> findRoleIdListByAdminId(Integer adminId);
    //2.删除所有已经分配的角色
    void deleteByAdminId(Integer adminId);
    //3.批量给用户分配角色
    void insertBatch(@Param("adminId")Integer adminId,@Param("roleIds") List<Integer> roleIds);
}
