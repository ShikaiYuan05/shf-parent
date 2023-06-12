package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Role;

import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
    private static final String LIST_ACTION = "redirect:/role";
    private static final String ASSIGN_SHOW = "role/assignShow";
    @Reference
    private RoleService roleService;

    @Reference
    private PermissionService permissionService;

    private static final String PAGE_INDEX = "role/index";

    @RequestMapping
    public String index(@RequestParam Map<String, String> filters, Model model) {
        //1. 调用业务层的方法进行分页查询
        PageInfo<Role> pageInfo = roleService.findPage(filters);
        //2. 将查询到的分页结果存储到请求域
        model.addAttribute("page", pageInfo);
        //3. 将搜索条件存储到请求域
        model.addAttribute("filters", filters);
        //4. 返回显示页面的逻辑视图
        return PAGE_INDEX;
    }

    //2.增
    @PostMapping("/insert")
    public String insert(Role role, Model model) {
        roleService.insert(role);
        return successPage("新增admin管理员用户信息成功",model);
    }

    //3.1进入修改页面
    @GetMapping("/edit/{id}")
    public String findById(@PathVariable("id") Integer id,Model model) {
        Role role = roleService.findById(id);
        model.addAttribute("role",role);
        return "role/edit";
    }


    //3.2修改
    @PostMapping("/update")
    public String update(Role role, Model model){
        roleService.update(role);
        return successPage("修改admin管理员用户信息成功",model);
    }

    //4.删
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id){
        roleService.delete(id);
        return LIST_ACTION;
    }

    @GetMapping("/assignShow/{roleId}")
    public String assignShow(@PathVariable("roleId") Long roleId,Model model){
        //1. 将roleId存储到request域中
        model.addAttribute("roleId",roleId);
        //2. 将ztree中需要JSON数据存入request域中
        List<Map<String,Object>> zNodes = permissionService.findZNodes(roleId);
        model.addAttribute("zNodes", JSON.toJSONString(zNodes));
        return ASSIGN_SHOW;
    }

    @PostMapping("/assignPermission")
    public String assignPermission(@RequestParam("roleId") Integer roleId,@RequestParam("permissionIds")List<Integer> permissionIds,Model model){
        //1. 调用业务层的方法给角色分配权限
        roleService.assignPermission(roleId,permissionIds);
        //2. 显示成功
        return successPage("袁世凯提示您：给角色分配权限成功",model);
    }

}
