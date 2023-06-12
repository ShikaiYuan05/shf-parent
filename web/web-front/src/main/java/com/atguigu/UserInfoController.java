package com.atguigu;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.vo.LoginVo;
import com.atguigu.entity.vo.RegisterVo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private UserInfoService userInfoService;

    @RequestMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable("phone")String phone){
        //1.生成验证码
        StringBuilder codeBuilder = new StringBuilder("");
        for (int i = 0; i < 4; i++) {
            codeBuilder.append((int)(Math.random()*10));
        }
        String code = codeBuilder.toString();
        //2.发给用户
        logger.info(phone+"的验证码为:{}",code);

        //3.保存到redis,过期时间
        Jedis jedis = jedisPool.getResource();
        jedis.setex("code:"+phone,60*5,code);
        jedis.close();

        //4.响应客户端,验证码发送成功
        return Result.ok();
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo){
        Jedis jedis = jedisPool.getResource();
        String code = jedis.get("code:" + registerVo.getPhone());
        //1.3比较
        if (registerVo.getCode().equals(code)){
            return userInfoService.doRegister(registerVo);
        }else {
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo, HttpSession session) {
        //1.调用 方法校验的登录
        Result result = userInfoService.doLogin(loginVo);
        //2.登录状态保存到session中
        if (Objects.equals(result.getCode(), ResultCodeEnum.SUCCESS.getCode())) {
            //login success will save info to session
            session.setAttribute("USER", result.getData());
        }
        return result;
    }

    @RequestMapping("/set/{sec}")
    public Result set(@PathVariable("sec") Integer sec,HttpSession session){
        session.setMaxInactiveInterval(sec);
        return Result.ok();
    }


    @GetMapping("/logout")
    public Result logout(HttpSession session){
        //1.invalidate the  session 使会话无效
        session.invalidate();
        return Result.ok();

    }
}
