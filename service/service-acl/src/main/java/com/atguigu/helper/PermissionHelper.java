package com.atguigu.helper;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.atguigu.entity.Permission;

import java.util.List;
import java.util.stream.Collectors;

public class PermissionHelper {

    public static List<Permission> build(List<Permission> permissionList){
        //1. 过滤出一级
        if (CollectionUtils.isNotEmpty(permissionList)) {
            List<Permission> menu = permissionList.stream()
                    .filter(permission -> permission.getParentId() == 0)
                    .collect(Collectors.toList());
            //2. 给menu中的每一个一级菜单设置children
            return menu.stream()
                    .map(permission -> {
                        //2.1 给每一个permission设置Level属性
                        permission.setLevel(1);
                        //2.2 给每一个permission设置children属性
                        //32.2.1 从permissionList中获取permission的所有子菜单
                        List<Permission> children = getChildren(permissionList, permission);
                        permission.setChildren(children);
                        //2.3 给每一个permission设置parentName属性,一级菜单没有父菜单
                        return permission;
                    }).collect(Collectors.toList());
        }
        return permissionList;
    }


    private static List<Permission> getChildren(List<Permission> permissionList, Permission permission) {
        List<Permission> children = permissionList.stream()
                .filter(p -> p.getParentId().equals(permission.getId())) // 过滤出父菜单ID与指定权限ID相等的权限
                .map(p -> {
                    //3.2.1 给每一个子菜单设置Level属性
                    p.setLevel(permission.getLevel() + 1);// 设置子菜单的层级为父菜单的层级加1
                    //3.2.2 给每一个子菜单设置parentName属性
                    p.setParentName(permission.getParentName());// 设置子菜单的父菜单名称为父菜单的名称
                    //3.2.1 给每一个子菜单设置children属性
                    p.setChildren(getChildren(permissionList,p));
                    return p;
                }).collect(Collectors.toList());
        return children;
    }
}
