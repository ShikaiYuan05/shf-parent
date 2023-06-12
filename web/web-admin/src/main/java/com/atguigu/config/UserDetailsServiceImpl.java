package com.atguigu.config;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Reference
    private AdminService adminService;
    @Reference
    private PermissionService permissionService;


    @Override
    public UserDetails loadUserByUsername(String username) {
        Admin admin = adminService.findByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<Permission> permissionList = permissionService.findPermissionListByAdminId(Math.toIntExact(admin.getId()));

        List<SimpleGrantedAuthority> grantedAuthorityList = new ArrayList<>();


        if (CollectionUtils.isNotEmpty(permissionList)) {
            grantedAuthorityList = permissionList.stream()
                    //过滤出三级菜单
                    .filter(permission -> permission.getType().equals(2))
                    .map(permission -> {
                        return new SimpleGrantedAuthority(permission.getCode());
                    })
                    .collect(Collectors.toList());
        }
        return new User(username,admin.getPassword(),grantedAuthorityList);
    }
}
