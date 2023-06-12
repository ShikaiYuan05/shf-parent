package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Permission;
import com.atguigu.helper.PermissionHelper;
import com.atguigu.mapper.PermissionMapper;
import com.atguigu.mapper.RolePermissionMapper;
import com.atguigu.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2023-06-06  14:54
 */
@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Override
    public BaseMapper<Permission> getBaseMapper() {
        return permissionMapper;
    }

    /*@Override
    public List<Map<String, Object>> findZNodes(Long roleId) {
        //1. 查询出所有的权限列表
        List<Permission> allPermissionList = permissionMapper.findAll();
        //2. 根据roleId查询出该角色已分配的权限的id列表
        List<Long> assignPermissionIdList = rolePermissionMapper.findPermissionIdListByRoleId(roleId);
        //3. 将allPermissionList转换成List<Map<String, Object>>并返回
        return allPermissionList.stream()
                .map(permission -> {
                    //3.1 创建map
                    Map<String,Object> map = new HashMap<>();
                    //3.2 设置map的键值对
                    //3.2.1 设置id
                    map.put("id",permission.getId());
                    //3.2.2 设置pId
                    map.put("pId",permission.getParentId());
                    //3.2.3 设置name
                    map.put("name",permission.getName());
                    //3.2.4 设置checked:是否选中,如果当前这个permission的id在assignPermissionIdList就说明这个permission是已分配的，checked就为true
                    if (CollectionUtils.isNotEmpty(assignPermissionIdList)) {
                        map.put("checked",assignPermissionIdList.contains(permission.getId()));
                    }
                    //3.2.5 设置open:是否展开
                    if (permission.getType() == 1) {
                        //如果是菜单，就展开
                        map.put("open",true);
                    }
                    return map;
                }).collect(Collectors.toList());
    }*/


    @Override
    public List<Map<String, Object>> findZNodes(Long roleId) {
        List<Permission> allPermissions = permissionMapper.findAll();
        List<Long> assignedPermissionIds = rolePermissionMapper.findPermissionIdListByRoleId(roleId);
        // 转化为HashSet以提高查询速度
        Set<Long> assignedPermissionIdSet = new HashSet<>(assignedPermissionIds);

        return allPermissions.stream()
                .map(permission -> convertPermissionToMap(permission, assignedPermissionIdSet))
                .collect(Collectors.toList());
    }

    /**
     * 将Permission对象转化为Map对象
     * @param permission
     * @param assignedPermissionIdSet
     * @return
     */
    private Map<String, Object> convertPermissionToMap(Permission permission, Set<Long> assignedPermissionIdSet) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", permission.getId());
        map.put("pId", permission.getParentId());
        map.put("name", permission.getName());

        // 优化后的代码，使用HashSet的contains方法提高速度
        map.put("checked", assignedPermissionIdSet.contains(permission.getId()));
        map.put("open", permission.getType() == 1);
        return map;
    }

    @Override
    public List<Permission> findMenu(Integer adminId){
        List<Permission> permissionList;
        if(adminId == 1){
            permissionList = permissionMapper.findAll();
        }else {
            permissionList=permissionMapper.findPermissionListByAdminId(adminId);
        }
        //2.构建父子关系
        return PermissionHelper.build(permissionList);
    }

    @Override
    public List<Permission> findPermissionListByAdminId(Integer adminId){
        if(adminId == 1){
           return permissionMapper.findAll();
        }else {
            return permissionMapper.findPermissionListByAdminId(adminId);
        }
    }
}
