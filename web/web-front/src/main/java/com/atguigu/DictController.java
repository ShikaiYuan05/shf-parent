package com.atguigu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/dict")
public class DictController {

    @Reference
    private DictService dictService;

    @RequestMapping("findDictListByParentDictCode/{dictCode}")
    public Result findDictListByParentDictCode(@PathVariable("dictCode")String dictCode){
        List<Dict> dicts = dictService.selectByParentDictCode(dictCode);
        return Result.ok(dicts);
    }
    @GetMapping("findDictListByParentId/{parentId}")
    public Result findDictListByParentId(@PathVariable("parentId")Integer parentId){
        List<Dict> DictList = dictService.findDictListByParentId(Long.valueOf(parentId));
        return Result.ok(DictList);
    }
}
