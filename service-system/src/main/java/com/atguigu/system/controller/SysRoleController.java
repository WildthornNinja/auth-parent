package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.vo.AssginRoleVo;
import com.atguigu.model.vo.SysRoleQueryVo;
import com.atguigu.system.annotation.Log;
import com.atguigu.system.enums.BusinessType;
import com.atguigu.system.exception.DiyException;
import com.atguigu.system.service.SysRoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 分页及带条件查询
     * @PathVariable注解 若方法参数名称和需要绑定的url中变量名称一致时,可以简写
     * @return
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.list')") //只有有'bnt.sysRole.list'权限才能调用以下方法
    @ApiOperation("分页及带条件查询")
    @GetMapping("/{current}/{size}")
    public Result findPage(
            @ApiParam(name = "current",value = "当前页码",required = true)
            @PathVariable Long current,
            @ApiParam(name = "size",value = "每条记录数",required = true)
            @PathVariable Long size,
            @ApiParam(name = "roleQueryVo",value = "查询条件[查询对象]",required = false)
            SysRoleQueryVo roleQueryVo){
        System.out.println("roleQueryVo = " + roleQueryVo.getRoleName());
        //创建Page对象
        Page<SysRole> page = new Page<>(current,size);
        IPage<SysRole> pageModel = sysRoleService.findPage(page, roleQueryVo);
        return Result.ok(pageModel);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')") //只有有'bnt.sysRole.list'权限才能调用以下方法
    @ApiOperation("查询所有信息，返回List")
    @GetMapping("/findAllReturnList")
    public List<SysRole> findAllReturnList(){
        /**
         * 测试统一异常处理
         */
        try {
            int i = 10/0;
        } catch (Exception e) {
            throw new DiyException("出现了自定义的异常DiyException",10001);
        }
        //调用sysRoleService 中查询方法
        List<SysRole> sysRoleList = sysRoleService.list();
        return sysRoleList;
    }
    @PreAuthorize("hasAuthority('bnt.sysRole.list')") //只有有'bnt.sysRole.list'权限才能调用以下方法
    @ApiOperation("查询所有信息，返回Result")
    @GetMapping("/findAll")
    public Result findAllReturnResult(){
        //调用sysRoleService 中查询方法
        List<SysRole> sysRoleList = sysRoleService.list();

        return Result.ok(sysRoleList);
    }
    @Log(title = "角色管理",businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation("添加角色")
    @PostMapping("/addRole")
    public Result addSysRole( @RequestBody SysRole sysRole){
        //调用sysRoleService 中 save() 方法
        sysRoleService.save(sysRole);
        return Result.ok();
    }
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("根据ID查询角色")
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable("id") Long id){
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }
    @Log(title = "角色管理",businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation("更新角色")
    @PutMapping("/updateSysRole")
    public Result updateSysRole(@RequestBody SysRole sysRole){
        boolean b = sysRoleService.updateById(sysRole);
        return Result.ok();
    }
    @Log(title = "角色管理",businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('bnt.sysRole.delete')")
    @ApiOperation("【逻辑】删除角色")
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Long id){
        sysRoleService.removeById(id);
        return Result.ok();
    }
    @Log(title = "角色管理",businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("批量删除")
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Long> ids){
        //调用SysRoleService 中 批量删除的方法
        sysRoleService.removeByIds(ids);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("根据用户获取角色数据")
    @GetMapping("/getRolesByUserId/{userId}")
    public Result toAssign(@PathVariable Long userId){
        //根据用户id 在 sys_user_role表中获取角色数据 返回角色id集合 和 用户id集合
        Map<String,Object> roleMap = sysRoleService.getRolesByUserId(userId);
        return Result.ok(roleMap);
    }
    @Log(title = "角色管理",businessType = BusinessType.ASSIGN)
    @PreAuthorize("hasAuthority('bnt.sysRole.assignAuth')")
    @ApiOperation("给用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo){
        System.out.println("assginRoleVo.getUserId() = " + assginRoleVo.getUserId());
        for (Long aLong : assginRoleVo.getRoleIdList()) {
            System.out.println("aLong = " + aLong);
        }
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok();
    }
}
