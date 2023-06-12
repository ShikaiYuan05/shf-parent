package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constants.SessionConstant;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.result.Result;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2023-06-06  09:30
 */
@RestController
@RequestMapping("/userFollow")
public class UserFollowController {
    @Reference
    private UserFollowService userFollowService;
    @GetMapping("/auth/follow/{houseId}")
    public Result addFollow(@PathVariable("houseId") Long houseId, HttpSession session){
        //1. 获取当前登录的用户id
        Map<String, Object> map = (Map<String, Object>) session.getAttribute(SessionConstant.USER_KEY);
        Long userId = (Long) map.get("id");
        //2. 远程调用业务层的方法添加关注记录
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(userId);
        userFollow.setHouseId(houseId);
        userFollowService.addFollow(userFollow);
        return Result.ok();
    }

    @GetMapping("/auth/list/{pageNum}/{pageSize}")
    public Result findPageList(@PathVariable("pageNum") Integer pageNum,
                               @PathVariable("pageSize") Integer pageSize,
                               HttpSession session){
        //1. 查询当前登录的用户的id
        Map<String,Object> map = (Map<String, Object>) session.getAttribute(SessionConstant.USER_KEY);
        Long id = (Long) map.get("id");
        //2. 远程调用业务层的方法查询当前登录用户的关注列表
        PageInfo<UserFollowVo> page = userFollowService.findPageList(pageNum,pageSize,id);
        return Result.ok(page);
    }

    @GetMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable("id") Integer id){
        //1. 远程调用业务层的方法根据id取消关注
        userFollowService.cancelFollow(id);
        return Result.ok();
    }
}
