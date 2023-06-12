package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.entity.Role;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2023-06-07  09:02
 */
@Controller
public class IndexController {
    private static final String PAGE_INDEX = "frame/index";
    @Reference
    private PermissionService permissionService;
    @Reference
    private AdminService adminService;
    @Reference
    private RoleService roleService;
    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request){


//        1. 调用业务层的方法根据用户id查询动态菜单
        //1.1 获取当前登录的用户的用户名
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        //1.2 根据username查询Admin
        Admin admin = adminService.findByUsername(username);
        //1.3 获取admin的id
        List<Permission> permissionList = permissionService.findMenu(Math.toIntExact(admin.getId()));
        //2. 将查询到的动态菜单放到model中
        model.addAttribute("permissionList",permissionList);
        //3. 将用户自身的信息存储到model中
        model.addAttribute("admin",admin);
        //4. 将用户拥有的角色列表存储到model中
        List<Role> roleList = roleService.findRoleListByAdminId(Math.toIntExact(admin.getId()));
        model.addAttribute("roleList",roleList);
        //5. 返回首页的逻辑视图名
        return PAGE_INDEX;
    }
}
