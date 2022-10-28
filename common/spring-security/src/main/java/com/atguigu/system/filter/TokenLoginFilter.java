package com.atguigu.system.filter;

import com.atguigu.common.helper.JwtHelper;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.common.util.IpUtil;
import com.atguigu.common.util.ResponseUtil;
import com.atguigu.model.vo.LoginVo;
import com.atguigu.system.custom.CustomUser;
import com.atguigu.system.service.LoginLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 定义用户认证过程的过滤器
 * 登录过滤器，继承UsernamePasswordAuthenticationFilter，对用户名密码进行登录校验
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private RedisTemplate redisTemplate;

    private LoginLogService loginLogService;

    public TokenLoginFilter(AuthenticationManager authenticationManager,RedisTemplate redisTemplate,LoginLogService loginLogService) {
        //设置认证管理器
        this.setAuthenticationManager(authenticationManager);
        //设置登录的请求地址及请求方式
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login", "POST"));
        //给当前对象的redisTemplate属性赋值
        this.redisTemplate = redisTemplate;
        this.loginLogService=loginLogService;
    }

    //用户认证调用的方法
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            //获取用户输入的用户名和密码，转换为LoginVo对象
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
            //创建一个没有认证的Authentication对象
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginVo.getUsername(),loginVo.getPassword());
            //通过AuthenticationManager对象进行认证
            return this.getAuthenticationManager().authenticate(authentication);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //认证成功调用的方法
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //通过认证之后的Authentication对象获取用户信息
        CustomUser customUser = (CustomUser) authResult.getPrincipal();
        //根据用户id和用户名生成token
        String token = JwtHelper.createToken(customUser.getSysUser().getId(), customUser.getSysUser().getUsername());
        Map map = new HashMap<>();
        map.put("token",token);

        //获取用户的权限
        Collection<GrantedAuthority> authorities = customUser.getAuthorities();
        //将用户的权限保存到Redis中
        redisTemplate.boundValueOps(customUser.getUsername()).set(authorities);

        //保存登录日志到数据库中
        loginLogService.recordLoginLog(customUser.getUsername(), IpUtil.getIpAddress(request),1,"登录成功");
        //将map写到前端
        ResponseUtil.out(response, Result.ok(map));
    }

    //认证失败调用的方法
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //给前端响应认证失败的信息
        ResponseUtil.out(response,Result.build(null, ResultCodeEnum.LOGIN_MOBLE_ERROR));
    }
}
