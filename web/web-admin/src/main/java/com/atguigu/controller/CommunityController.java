package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController {

    public static final String PAGE_INDEX = "community/index";
    private static final String PAGE_CREATE = "community/create";
    private static final String PAGE_EDIT = "community/edit";
    private static final String LIST_ACTION="redirect:/community";
    @Reference
    private CommunityService communityService;
    @Reference
    private DictService dictService;

    @RequestMapping
    public String index(@RequestParam Map<String, String> filters, Model model) {
        PageInfo<Community> page = communityService.findPage(filters);
        //2. 将查询到的分页结果存储到请求域
        model.addAttribute("page", page);

        //判断如果filters中没有，我们默认给它赋一个0
        filters.putIfAbsent("areaId", "0");
        filters.putIfAbsent("plateId", "0");
        //3. 将搜索条件存储到请求域回显
        model.addAttribute("filters", filters);

        List<Dict> dictListByParentId = dictService.findDictListByParentId(110000L);

        //3. 调用业务层的方法查询北京的所有区域，并存储到请求域
        model.addAttribute("areaList", dictListByParentId);

        //4. 返回显示页面的逻辑视图
        return PAGE_INDEX;
    }

    //转发到新增页面
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("areaList", dictService.findDictListByParentId(110000L));
        return PAGE_CREATE;
    }

    @PostMapping("/save")
    public String save(Community community, Model model) {
        communityService.insert(community);
        return successPage("新增小区成功", model);
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Community byId = communityService.findById(id);
        //1. 将小区信息存入request域中
        model.addAttribute("community", communityService.findById(id));
        //2. 查询北京的所有区域信息,存入request域中
        model.addAttribute("areaList", dictService.findDictListByParentId(110000L));
        return PAGE_EDIT;
    }


    @PostMapping("/update")
    public String update(Community community, Model model) {
        //1. 调用业务层的方法修改小区信息
        communityService.update(community);
        //2. 显示成功页面
        return successPage("修改小区成功", model);
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id){
        //1. 调用业务层的方法根据id删除小区信息
        communityService.delete(id);
        //2. 重定向访问小区的首页
        return LIST_ACTION;
    }
}


