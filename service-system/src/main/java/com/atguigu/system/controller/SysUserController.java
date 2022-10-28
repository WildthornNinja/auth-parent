package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.common.util.MD5;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.SysUserQueryVo;
import com.atguigu.system.service.SysUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags="用户管理")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService userService;
    /**
     * 分页及带条件查询
     * @PathVariable注解 若方法参数名称和需要绑定的url中变量名称一致时,可以简写
     * @return
     */
    @ApiOperation("分页及带条件查询")
    @GetMapping("/{current}/{size}")
    public Result findPage(@ApiParam(name = "current",value = "当前页码",required = true)
                           @PathVariable Long current,
                           @ApiParam(name="size",value = "每条记录数",required = true)
                           @PathVariable Long size,
                           @ApiParam(name = "userQueryVo",value = "查询条件[查询对象]",required = false)
                                       SysUserQueryVo userQueryVo){
        Page<SysUser> page = new Page<SysUser>(current,size);
        IPage<SysUser> pageModel = userService.findPage(page, userQueryVo);
        return Result.ok(pageModel);
    }
    @ApiOperation("更改用户状态")
    @GetMapping("/updateStatus/{userId}/{status}")
    public Result updateStatus(@PathVariable Long userId,@PathVariable Integer status){
        userService.updateUserStatus(userId,status);
        return Result.ok();
    }

    @ApiOperation("添加用户")
    @PostMapping("/addUser")
    public Result addUser(@RequestBody SysUser sysUser){
        sysUser.setHeadUrl("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
        System.out.println("sysUser.getName() = " + sysUser.getName());
        //对用户密码进行MD5加密
        sysUser.setPassword(MD5.encrypt(sysUser.getPassword()));
        userService.save(sysUser);
        return Result.ok();
    }

    @ApiOperation("根据ID查询用户")
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable Long id){
        System.out.println("id = " + id);
        SysUser sysUser = userService.getById(id);
        return Result.ok(sysUser);
    }

    @ApiOperation("更新用户")
    @PutMapping("/updateUser")
    public Result updateUser(@RequestBody SysUser sysUser){
        userService.updateById(sysUser);
        return Result.ok();
    }

    @ApiOperation("【逻辑】删除用户")
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Long id){
        userService.removeById(id);
        return Result.ok();
    }
}
