package com.atguigu.system.controller;

import com.atguigu.common.helper.JwtHelper;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.common.util.MD5;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.LoginVo;
import com.atguigu.system.exception.DiyException;
import com.atguigu.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api("前端登陆")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("用户登陆")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo){
        //{"code":20000,"data":{"token":"admin-token"}}
        String username = loginVo.getUsername();
        //根据用户名查询数据库，调用SysUserService，
        SysUser sysUser = sysUserService.getSysUserByUserName(username);
        //判断用户名是否存在
        if(sysUser == null){
            //证明用户不存在
            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
        }
        //判断密码是否正确
        if(!MD5.encrypt(loginVo.getPassword()).equalsIgnoreCase(sysUser.getPassword())){
            throw new DiyException(ResultCodeEnum.PASSWORD_ERROR);
        }
        //判断账号是否被锁定
        if(sysUser.getStatus()==0){
            //账号被锁定
            throw  new DiyException(ResultCodeEnum.ACCOUNT_STOP);
        }
        //根据用户id和用户名生成token
        String token = JwtHelper.createToken(sysUser.getId(), username);
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        return Result.ok(map);
    }
    @ApiOperation("获取登陆用户信息")
    @GetMapping("/info")
    public Result getInfo(HttpServletRequest request){
        Long userId = JwtHelper.getUserId(request.getHeader("token"));
        Map<String,Object> userInfoMap = sysUserService.getUserInfoById(userId);
        return Result.ok(userInfoMap);
    }
    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result logOut(){
        return  Result.ok();
    }
}
