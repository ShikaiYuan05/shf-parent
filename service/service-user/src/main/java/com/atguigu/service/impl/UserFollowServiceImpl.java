package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constants.PaginationConstant;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.mapper.UserFollowMapper;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = UserFollowService.class)
public class UserFollowServiceImpl implements UserFollowService {
    @Autowired
    private UserFollowMapper userFollowMapper;
    @Override
    public UserFollow findByUserIdAndHouseId(Integer userId, Integer houseId) {
        return userFollowMapper.findByUserIdAndHouseId(userId,houseId);
    }

    @Override
    public boolean addFollow(UserFollow userFollow) {
        return userFollowMapper.insert(userFollow);
    }

    @Override
    public PageInfo<UserFollowVo> findPageList(Integer pageNum, Integer pageSize, Long userId) {
        //1. 开启分页
        PageHelper.startPage(pageNum,pageSize);
        //2. 调用持久层根据用户id查询关注列表
        List<UserFollowVo> userFollowVoList =  userFollowMapper.findListByUserId(userId);
        return new PageInfo<>(userFollowVoList, PaginationConstant.DEFAULT_NAVIGATE_PAGES);
    }

    @Override
    public boolean cancelFollow(Integer id) {
        return userFollowMapper.delete(id);
    }
}
