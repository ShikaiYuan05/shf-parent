package com.atguigu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 包名:com.atguigu.config
 *
 * @author Leevi
 * 日期2023-06-07  10:26
 */
@Configuration
@EnableWebSecurity  //自动应用 Spring Security 的默认配置，并将其集成到 Spring Boot 应用程序中
@EnableGlobalMethodSecurity(prePostEnabled = true) //通过设置 prePostEnabled = true，可以在方法上使用 @PreAuthorize 和 @PostAuthorize 注解来实现基于表达式的方法级安全性控制
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user")
//                .password(passwordEncoder.encode("123456"))
//                .roles("");
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //允许iframe嵌套显示
        http.headers().frameOptions().disable();

        http
            .authorizeRequests()
            .antMatchers("/static/**","/loginPage").permitAll()  //哪些请求可以放行
            .anyRequest().authenticated()    // 其它页面全部需要验证
            .and()
            .formLogin()
            .loginProcessingUrl("/login") //指定处理登录请求的路径
            .loginPage("/loginPage")    //用户未登录时，访问任何需要权限的资源都转跳到该路径，即登录页面，此时登陆成功后会继续跳转到第一次访问的资源页面（相当于被过滤了一下）
            .defaultSuccessUrl("/") //登录认证成功后默认转跳的路径
            .and()
            .logout()
            .logoutUrl("/logout")   //退出登陆的路径，指定spring security拦截的注销url,退出功能是security提供的
            .logoutSuccessUrl("/loginPage");//用户退出后要被重定向的url
        //关闭跨域请求伪造
        http.csrf().disable();

        //设置访问拒绝的页面
        //http.exceptionHandling().accessDeniedPage("/auth");

        //设置访问拒绝的处理器
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }
}
