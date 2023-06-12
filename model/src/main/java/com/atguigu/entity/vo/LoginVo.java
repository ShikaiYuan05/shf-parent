package com.atguigu.entity.vo;


import lombok.Data;

import java.io.Serializable;

/**
 * 登录对象
 */
@Data
public class LoginVo implements Serializable {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;
}
