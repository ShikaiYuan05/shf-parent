package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.en.HouseImageType;
import com.atguigu.en.HouseStatus;
import com.atguigu.entity.*;
import com.atguigu.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {

    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CREATE = "house/create";
    private static final String PAGE_EDIT = "house/edit";
    private static final String LIST_ACTION = "redirect:/house";
    private static final String PAGE_SHOW = "house/show";

    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseUserService houseUserService;
    @RequestMapping
    public String index(@RequestParam Map<String,String> filters, Model model){
        //1. 调用业务层的方法查询首页信息
        Map<String, Object> indexInfo = houseService.findHouseIndexInfo(filters);
        //2. 将indexInfo这个Map中的所有键值对存储到请求域
        model.addAllAttributes(indexInfo);
        return PAGE_INDEX;}


        @GetMapping("/create")
        public String create(Model model){
            //查询初始化列表
            Map<String, Object> initList = houseService.findInitList();
            //将initList存储到请求域中
            model.addAllAttributes(initList);
            return PAGE_CREATE;
        }

        @PostMapping("/save")
        public String save(House house, Model model){
            //1. 调用业务层的方法保存房源信息
            //保存之前手动给status字段赋值为未发布
            house.setStatus(HouseStatus.UNPUBLISHED.getCode());//现在是0表示未发布
            houseService.insert(house);
            //2. 显示成功页面
            return successPage("新增房源成功",model);
        }

        @GetMapping("/edit/{id}")
        public String edit(@PathVariable("id") Integer id, Model model){
            //1. 调用业务层的方法查询房源信息
            House house = houseService.findById(id);
            //2. 将房源信息存储到请求域中
            model.addAttribute("house",house);
            //3. 查询初始化列表
            Map<String, Object> initList = houseService.findInitList();
            //4. 将initList存储到请求域中
            model.addAllAttributes(initList);
            //3. 跳转到修改页面
            return PAGE_EDIT;
        }

        @PostMapping("/update")
        public String update(House house,Model model){
            //1. 调用业务层的方法更新房源信息
            houseService.update(house);
            //2. 显示成功页面
            return successPage("修改房源信息成功",model);
        }

        @GetMapping("/delete/{id}")
        public String delete(@PathVariable("id") Integer id){
            //1. 调用业务层的方法删除房源信息
            houseService.delete(id);
            //2. 重定向访问房源首页
            return LIST_ACTION;
        }

        @GetMapping("/publish/{id}/{status}")
        public String publish(@PathVariable("id") Integer id,@PathVariable("status") Integer status){
            //1. 调用业务层的方法发布或者取消发布房源
            House house = new House();
            house.setId(Long.valueOf(id));
            house.setStatus(status);
            houseService.update(house);
            //2. 重定向访问房源首页
            return LIST_ACTION;
        }

        @RequestMapping("/{id}")
        public String show(@PathVariable("id") Integer id,Model model){
            //1.查询房源自身信息
            House house = houseService.findById(id);
            model.addAttribute("house",house);
            //2. 查询房源所在的小区信息
            Community community = communityService.findById(Math.toIntExact(house.getCommunityId()));
            model.addAttribute("community",community);
            //3. 查询房源的房源图片列表
            List<HouseImage> houseImage1List = houseImageService.getHouseImageListByHouseId(id, HouseImageType.HOUSE_SOURCE.getTypeCode());
            model.addAttribute("houseImage1List",houseImage1List);
            //4. 查询房源的房产图片列表
            List<HouseImage> houseImage2List = houseImageService.getHouseImageListByHouseId(id, HouseImageType.HOUSE_PROPERTY.getTypeCode());
            model.addAttribute("houseImage2List",houseImage2List);
            //5. 查询房源的经纪人列表
            List<HouseBroker> houseBrokerList = houseBrokerService.findHouseBrokerByHouseId(id);
            model.addAttribute("houseBrokerList",houseBrokerList);
            //6. 查询房源的房东列表
            List<HouseUser> houseUserList = houseUserService.getHouseUserListByHouseId(id);
            model.addAttribute("houseUserList",houseUserList);
            return PAGE_SHOW;
        }
    }

