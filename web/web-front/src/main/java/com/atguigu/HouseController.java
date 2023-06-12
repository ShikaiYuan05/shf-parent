package com.atguigu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.vo.HouseQueryVo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.result.Result;
import com.atguigu.service.HouseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/house")
public class HouseController {

    @Reference
    private HouseService houseService;

    @RequestMapping("/list/{pageNum}/{pageSize}")
    public Result findPage(@PathVariable("pageNum") Integer pageNum,
                           @PathVariable("pageSize") Integer pageSize,
                           @RequestBody HouseQueryVo houseQueryVo) {
        //1.调用业务层方法查询房源分页信息
        PageInfo<HouseVo> page = houseService.findListPage(pageNum, pageSize, houseQueryVo);
        //2.响应
        return Result.ok(page);
    }

    @RequestMapping("/info/{houseId}")
    public Result info(@PathVariable("houseId") Integer houseId) {
        //调用业务层方法查询房源详情
        Map<String, Object> map = houseService.findHouseDetail(houseId);
        //2.远程调用会员服务查询当前会员是否关注todo
        //默认false
        map.put("isFollow", false);
        return Result.ok(map);
    }
}
