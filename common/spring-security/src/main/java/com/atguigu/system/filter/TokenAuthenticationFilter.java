package com.atguigu.system.filter;

import com.atguigu.common.helper.JwtHelper;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.common.util.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
/**
 * 认证解析token过滤器
 * 通过获取请求头的token中的用户名实现用户认证的过滤器
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求地址
        String requestURI = request.getRequestURI();
        //判断用户是否在登录,如果是登陆接口 直接放行 不进行拦截
        if("/admin/system/index/login".equals(requestURI)){
            //证明是登录的请求，放行
            filterChain.doFilter(request,response);
            //不要再执行下面的代码了
            return;
        }

        //获取认证之后的对象
        Authentication authentication = getAuthenticated(request);
        //判断
        if(null != authentication){
            //认证成功，将认证之后的对象放到上下文中
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //放行请求
            filterChain.doFilter(request,response);
        }else{
            //证明没有权限
            ResponseUtil.out(response, Result.build(null,ResultCodeEnum.PERMISSION));
        }
    }

    //获取已认证对象的方法
    private Authentication getAuthenticated(HttpServletRequest request) {
        //获取请求头中的token
        String token = request.getHeader("token");
        if(!StringUtils.isEmpty(token)){
            //从token中获取用户名
            String username = JwtHelper.getUsername(token);
            if(!StringUtils.isEmpty(username)){
                //从Redis中获取用户的权限
                Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) redisTemplate.boundValueOps(username).get();

                //创建已经认证的Authentication对象
                UsernamePasswordAuthenticationToken authenticated = new UsernamePasswordAuthenticationToken(username, null, authorities);
                //返回已经认证之后的对象
                return authenticated;
            }
        }
        return null;
    }
}
