package com.atguigu.service;

import com.atguigu.entity.vo.LoginVo;
import com.atguigu.entity.vo.RegisterVo;
import com.atguigu.result.Result;

public interface UserInfoService {
    /**
     * 执行注册
     * @param registerVo
     */
    Result doRegister(RegisterVo registerVo);

    /**
     * 校验登录
     * @param loginVo
     * @return
     */
    Result doLogin(LoginVo loginVo);
}
