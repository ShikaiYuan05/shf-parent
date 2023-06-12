package com.atguigu.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseService;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Role;
import com.atguigu.entity.RolePermission;
import com.atguigu.mapper.AdminRoleMapper;
import com.atguigu.mapper.RoleMapper;
import com.atguigu.mapper.RolePermissionMapper;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public BaseMapper<Role> getBaseMapper() {
        return roleMapper;
    }

    @Override
    public Map<String, List<Role>> findRoleListMapByAdminId(Integer adminId) {
        //1. 查询出所有角色列表
        List<Role> allRoleList = roleMapper.findAll();
        //2. 查询出用户已分配的角色的id列表
        List<Long> assignRoleIdList = adminRoleMapper.findRoleIdListByAdminId(adminId);
        //3. 遍历allRoleList
        List<Role> assignRoleList = new ArrayList<>();
        List<Role> unAssignRoleList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(allRoleList) && CollectionUtils.isNotEmpty(assignRoleIdList)) {
            //过滤出已分配的角色列表
            Map<Boolean, List<Role>> collect = allRoleList.stream()
                    .collect(Collectors.partitioningBy(role -> assignRoleIdList.contains(role.getId())));

            assignRoleList = collect.get(true);
            unAssignRoleList=collect.get(false);



//            for (Role role : allRoleList) {
//                if (assignRoleIdList.contains(role.getId())) {
//                    assignRoleList.add(role);
//                } else {
//                    unAssignRoleList.add(role);
//                }
//            }
        } else if (CollectionUtils.isEmpty(assignRoleIdList)) {
            //已分配的角色类表为空，未分配的角色列表就是所有角色列表
            unAssignRoleList = allRoleList;
        }
        //将assignRoleList和unAssignRoleList封装到Map中
        Map<String, List<Role>> roleListMap = new HashMap<>();
        roleListMap.put("assignRoleList", assignRoleList);
        roleListMap.put("unAssignRoleList", unAssignRoleList);
        return roleListMap;
    }



    @Override
    public void assignPermission(Integer roleId, List<Integer> permissionIds) {
        //1. 先删除该角色之前分配的权限
        rolePermissionMapper.deleteByRoleId(roleId);
        //2. 再给该角色分配新的权限
        if (CollectionUtils.isNotEmpty(permissionIds)) {
            rolePermissionMapper.insertBatch(roleId, permissionIds);
        }
    }

    @Override
    public List<Role> findRoleListByAdminId(Integer adminId) {
        return roleMapper.findRoleListByAdminId(adminId);
    }
}