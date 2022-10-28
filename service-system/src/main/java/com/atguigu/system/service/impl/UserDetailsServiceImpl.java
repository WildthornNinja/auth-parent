package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysUser;
import com.atguigu.system.service.SysUserService;
import com.atguigu.system.custom.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //调用sysUserService 中按照用户名查询得到SysUser对象的方法
        SysUser sysUser = sysUserService.getSysUserByUserName(username);
        if(sysUser ==null){
            throw  new UsernameNotFoundException("用户名不正确");
        }
        if(sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("账号已停用");
        }
        //授权
        //根据用户id查询用户的按钮权限标识符
        List<String> userBtnPers = sysUserService.getUserBtnPermsByUserId(sysUser.getId());
        //创建一个保存权限的集合
        List<GrantedAuthority> authorities = new ArrayList<>();
        //遍历所有的按钮权限标识符
        for (String userBtnPer : userBtnPers) {
            authorities.add(new SimpleGrantedAuthority(userBtnPer));
        }
        //返回自定义的用户对象
        return new CustomUser(sysUser, authorities);
    }
}
