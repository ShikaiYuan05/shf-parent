package com.atguigu.mapper;

import com.atguigu.entity.UserInfo;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2023-06-05  11:31
 */
public interface UserInfoMapper {

    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return
     */
    UserInfo findByPhone(String phone);

    /**
     * 保存用户信息
     * @param nickName
     * @return
     */
    UserInfo findByNickName(String nickName);

    /**
     * 保存用户信息
     * @param userInfo
     */
    void insert(UserInfo userInfo);
}
