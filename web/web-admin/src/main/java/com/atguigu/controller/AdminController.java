package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Role;
import com.atguigu.service.AdminService;
import com.atguigu.service.RoleService;
import com.atguigu.util.FileUtil;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    private static final String PAGE_UPLOAD_SHOW = "admin/upload";
    private static final String PAGE_INDEX = "admin/index";
    private static final String PAGE_ASSIGN_SHOW = "admin/assignShow";;
    @Reference
    private AdminService adminService;
    @Reference
    private RoleService roleService;

    //1.查
    @PreAuthorize("hasAuthority('admin.show')")
    @RequestMapping
    public String index(@RequestParam Map<String, String> filters, Model model) {
        //1. 调用业务层的方法查询到所有角色
//        List<Admin> adminList = adminService.findAll();
        PageInfo<Admin> pageInfo = adminService.findPage(filters);
        //2. 将查询到的所有角色存储到请求域中
        model.addAttribute("page", pageInfo);
        // 回显作用
        model.addAttribute("filters", filters);
        return PAGE_INDEX;
    }

    //2.增
    @PreAuthorize("hasAuthority('admin.create')")
    @PostMapping("/insert")
    public String insert(Admin admin, Model model) {
        adminService.insert(admin);
       return successPage("新增admin管理员用户信息成功",model);
    }

    //3.改
    @PreAuthorize("hasAuthority('admin.edit')")
    @PostMapping("/update")
    public String update(Admin admin,Model model){
        adminService.update(admin);
        return successPage("修改admin管理员用户信息成功",model);
    }

    @PreAuthorize("hasAuthority('admin.upload')")
    @GetMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable("id") Integer id,Model model){
        //将id存入请求域
        model.addAttribute("id",id);
        //显示头像上传页面
        return PAGE_UPLOAD_SHOW;
    }

    @PreAuthorize("hasAuthority('admin.edit')")
    @PostMapping("/upload/{id}")
    public String upload(@RequestParam("file")MultipartFile multipartFile, @PathVariable("id") Integer id,Model model) throws IOException {
        //1. 将客户端上传的头像存入七牛云:要求文件名唯一、并且要有两级随机目录
        //1.1 获取唯一文件名
        String uuidName = FileUtil.getUUIDName(multipartFile.getOriginalFilename());
        //1.2 随机的两级目录
        String randomDirPath = FileUtil.getRandomDirPath(2, 2);
        //1.3 拼接图片在七牛云的存储路径
        String filePath = randomDirPath + uuidName;
        //1.4 上传到七牛云
        QiniuUtils.upload2Qiniu(multipartFile.getBytes(),filePath);
        //2. 修改admin的head_url
        //2.1 获取图片的访问路径
        String url = QiniuUtils.getUrl(filePath);
        Admin admin = new Admin();
        admin.setId(Long.valueOf(id));
        admin.setHeadUrl(url);
        //2.2 修改admin
        adminService.update(admin);
        //3. 显示成功页面
        return successPage("头像上传成功",model);
    }

    @PreAuthorize("hasAuthority('admin.edit')")
    @GetMapping("/edit/{id}")
    public String findById(@PathVariable("id") Integer id,Model model){
        Admin admin = adminService.findById(id);
        model.addAttribute("admin",admin);
        return "admin/edit";
    }

    @PreAuthorize("hasAuthority('admin.delete')")
    //4.删
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id){
        adminService.delete(id);
        return "redirect:/admin";
    }

    @PreAuthorize("hasAuthority('admin.assign')")
    @GetMapping("/assignShow/{id}")
    public String assignShow(@PathVariable("id") Integer id,Model model){
        //1.查已经 和未 两列表
        Map<String, List<Role>> roleListMap = roleService.findRoleListMapByAdminId(id);
        model.addAllAttributes(roleListMap);
        model.addAttribute("adminId",id);
        return PAGE_ASSIGN_SHOW;
    }

    @PreAuthorize("hasAuthority('admin.assign')")
    @PostMapping("/assignRole")
    public String assignRole(@RequestParam("adminId")Integer adminId,@RequestParam("roleIds") List<Integer> roleIds,Model model){
        adminService.assignRole(adminId,roleIds);
        return successPage("袁世凯提示您：分配角色成功",model);

    }
}