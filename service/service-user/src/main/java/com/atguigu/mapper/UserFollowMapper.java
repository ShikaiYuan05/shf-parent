package com.atguigu.mapper;

import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFollowMapper {
    UserFollow findByUserIdAndHouseId(@Param("userId") Integer userId, @Param("houseId") Integer houseId);

    boolean insert(UserFollow userFollow);

    List<UserFollowVo> findListByUserId(Long userId);

    boolean delete(Integer id);
}
