package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dict")
public class DictController {
    private static final String PAGE_INDEX="dict/index";
    @Reference
    private DictService dictService;


    @RequestMapping
    public String index(){
        return  PAGE_INDEX;
    }


    @RequestMapping("/findZnodes")
    @ResponseBody
    public Result findZnodes(@RequestParam(value = "id",defaultValue = "0")Long id){

        List<Map<String, Object>> maps = dictService.selectByParentId(id);
        return Result.ok(maps);
    }

    @ResponseBody
    @GetMapping("/findDictListByParentId/{parentId}")
    public Result findDictListByParentId(@PathVariable("parentId") Long parentId){
        List<Dict> dictListByParentId = dictService.findDictListByParentId(parentId);
        return Result.ok(dictListByParentId);
    }
}
