package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.en.UserStatus;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.vo.LoginVo;
import com.atguigu.entity.vo.RegisterVo;
import com.atguigu.mapper.UserInfoMapper;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2023-06-05  11:24
 */
@Service(interfaceClass = UserInfoService.class)
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Override
    public Result doRegister(RegisterVo registerVo) {
        //0. 判断手机号是否已经被注册了
        UserInfo dbUserInfo = userInfoMapper.findByPhone(registerVo.getPhone());
        if (dbUserInfo != null) {
            //查到有,就是手机号已经被注册了
            return Result.build(null, ResultCodeEnum.PHONE_REGISTER_ERROR);
        }
        //1. 判断昵称是否已经被占用
        dbUserInfo = userInfoMapper.findByNickName(registerVo.getNickName());
        if (dbUserInfo != null) {
            //昵称已经被占用
            return Result.build(null, ResultCodeEnum.NICKNAME_REGISTER_ERROR);
        }

        //2. 将RegisterVO对象转换成UserInfo对象
        UserInfo userInfo = new UserInfo();
        //2.1 属性拷贝
        BeanUtils.copyProperties(registerVo,userInfo);

        //2.2 设置status
        userInfo.setStatus(UserStatus.NORMAL.getStatusCode());
        //2.3 对密码进行加密
        userInfo.setPassword(MD5.encrypt(userInfo.getPassword()));
        //3. 调用持久层的方法保存用户信息
        userInfoMapper.insert(userInfo);
        return Result.ok();
    }

    @Override
    public Result doLogin(LoginVo loginVo) {
        //1. 校验手机号是否正确:根据手机号查询用户信息
        UserInfo dbUserInfo = userInfoMapper.findByPhone(loginVo.getPhone());
        if (dbUserInfo == null) {
            //说明手机号错误
            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
        }
        //2. 账号是否被锁定
        if (dbUserInfo.getStatus().equals(UserStatus.LOCKED.getStatusCode())) {
            //说明账号被锁定
            return Result.build(null, ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }
        //3. 密码是否正确
        if (!dbUserInfo.getPassword().equals(MD5.encrypt(loginVo.getPassword()))) {
            //说明密码错误
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
        }
        //4. 登录成功
        Map map = new HashMap<>();
        map.put("id",dbUserInfo.getId());
        map.put("phone",dbUserInfo.getPhone());
        map.put("nickName",dbUserInfo.getNickName());
        return Result.ok(map);
    }
}
