package com.atguigu.service;

import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

public interface UserFollowService {

//    查询是否关注房源
    UserFollow findByUserIdAndHouseId(Integer userId,Integer houseId);

    //添加关注记录
    boolean addFollow(UserFollow userFollow);

    //查用户关注列表的分页信息
    PageInfo<UserFollowVo> findPageList(Integer pageNum,Integer pageSize,Long userId);

    boolean cancelFollow(Integer id);


}
