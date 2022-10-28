package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysDept;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.vo.AssginMenuVo;
import com.atguigu.system.annotation.Log;
import com.atguigu.system.enums.BusinessType;
import com.atguigu.system.service.SysDeptService;
import com.atguigu.system.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/admin/system/sysDept")
public class SysDeptController {
    @Autowired
    private SysDeptService sysDeptService;

    @ApiOperation(value = "获取全部部门节点")
    @PreAuthorize("hasAuthority('bnt.sysDept.list')")
    @GetMapping("findNodes")
    public Result findNodes() {
        //sysDeptService 中 查询所有部门并转换为部门树对象
        List<SysDept> sysDepts = sysDeptService.findDeptNodes();
        return Result.ok(sysDepts);
    }

    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @ApiOperation("添加子节点")
    @PreAuthorize("hasAuthority('bnt.sysDept.add')")
    @PostMapping("/save")
    public Result saveNode(@RequestBody SysDept sysDept){
        sysDeptService.save(sysDept);
        return Result.ok();
    }
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @ApiOperation("删除节点")
    @PreAuthorize("hasAuthority('bnt.sysDept.remove')")
    @DeleteMapping("/remove/{id}")
    public Result deleteNode(@PathVariable Long id){
        sysDeptService.removeById(id);
        return Result.ok();
    }
    @ApiOperation("根据id查询节点")
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable Long id){
        SysDept sysDept = sysDeptService.getById(id);
        return Result.ok(sysDept);
    }
    @ApiOperation(value = "获取用户部门节点")
    @GetMapping("findUserNodes")
    public Result findUserNodes() {
        return Result.ok(sysDeptService.findUserNodes());
    }
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @ApiOperation("更新节点")
    @PreAuthorize("hasAuthority('bnt.sysDept.update')")
    @PutMapping("/update")
    public Result updateNode(@RequestBody SysDept sysDept){
        sysDeptService.updateById(sysDept);
        return Result.ok();
    }



}
