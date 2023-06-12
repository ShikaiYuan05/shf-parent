package com.atguigu;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(JUnit4.class)
public class PhotoTest{

    @Autowired
    private AdminService adminService;

    @Autowired
    private PermissionService permissionService;

    @Test
    public void Test01(){
        Admin xukun = adminService.findByUsername("xukun");
        if (xukun==null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        //根据id找出所有权限
        List<Permission> permissionList = permissionService.findPermissionListByAdminId(Math.toIntExact(xukun.getId()));
        //防止空指针
        List<SimpleGrantedAuthority> grantedAuthority = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(permissionList)){
            List<Object> collect = permissionList.stream()
                    .filter(permission -> permission.getType().equals(2))
                    .map(permission -> {
                    //return关键字将创建的SimpleGrantedAuthority对象作为当前代码块的返回值。
                                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permission.getCode());
                                System.out.println("simpleGrantedAuthority = " + simpleGrantedAuthority);
                                return simpleGrantedAuthority;
                            }
                    )
                    .collect(Collectors.toList());
        }
    }
}