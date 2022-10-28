package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.vo.AssginMenuVo;
import com.atguigu.system.service.SysMenuService;
import com.atguigu.system.service.impl.SysMenuServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation("查询菜单列表")
    @GetMapping("/findMenuNodes")
    public Result findMenuNodes() {
        //调用sysMenuService 中 查询所有菜单并转换为菜单树对象
        List<SysMenu> sysMenus = sysMenuService.findMenuNodes();
        return Result.ok(sysMenus);
    }

    @ApiOperation("添加子节点")
    @PostMapping("/save")
    public Result saveNode(@RequestBody SysMenu sysMenu){
        sysMenuService.save(sysMenu);
        return Result.ok();
    }
    @ApiOperation("删除节点")
    @DeleteMapping("/delete/{id}")
    public Result deleteNode(@PathVariable Long id){
        sysMenuService.deleteEmptyNode(id);
        return Result.ok();
    }
    @ApiOperation("根据id查询节点")
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable Long id){
        SysMenu sysMenu = sysMenuService.getById(id);
        return Result.ok(sysMenu);
    }
    @ApiOperation("更新节点")
    @PutMapping("/update")
    public Result updateNode(@RequestBody SysMenu sysMenu){
        sysMenuService.updateById(sysMenu);
        return Result.ok();
    }

    @ApiOperation(value = "根据角色获取菜单数据")
    @GetMapping("/getRoleMenuList/{roleId}")
    public Result toAssign(@PathVariable Long roleId){
        //通过角色id 获取该角色所拥有的权限 即关联的菜单列表
        List<SysMenu> sysMenuList = sysMenuService.getMenuByRoleId(roleId);
        return Result.ok(sysMenuList);

    }

    @ApiOperation("给角色分配所拥有的菜单")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody  AssginMenuVo assginMenuVo){
        System.out.println("assginMenuVo.getMenuIdList() = " + assginMenuVo.getMenuIdList());
        System.out.println("assginMenuVo.getRoleId() = " + assginMenuVo.getRoleId());
        sysMenuService.doAssign(assginMenuVo);
        return Result.ok();
    }




}
