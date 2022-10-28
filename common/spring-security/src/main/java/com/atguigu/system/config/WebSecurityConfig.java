package com.atguigu.system.config;

import com.atguigu.system.custom.CustomMD5PasswordEncoder;
import com.atguigu.system.filter.TokenAuthenticationFilter;
import com.atguigu.system.filter.TokenLoginFilter;
import com.atguigu.system.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@SpringBootConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启Controller方法的权限控制
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomMD5PasswordEncoder customMD5PasswordEncoder;

    @Autowired
    private LoginLogService loginLogService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //禁用CSRF功能
        http.csrf().disable();
        //设置那些请求不需要认证，那些请求需要认证
        http.authorizeRequests().antMatchers("/admin/system/index/login","/admin/system/index/info","/admin/system/index/logout").permitAll().anyRequest().authenticated();
        //添加自定义的过滤器
        http.addFilter(new TokenLoginFilter(authenticationManager(),redisTemplate,loginLogService));
        //配置TokenAuthenticationFilter在UsernamePasswordAuthenticationFilter过滤器之前执行
        http.addFilterBefore(new TokenAuthenticationFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //设置UserDetailsService和密码加密器
        auth.userDetailsService(userDetailsService).passwordEncoder(customMD5PasswordEncoder);
    }

    /**
     * 配置哪些请求不拦截
     * 排除swagger相关请求
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favicon.ico","/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/doc.html");
    }
}
