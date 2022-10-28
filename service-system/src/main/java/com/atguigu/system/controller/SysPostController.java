package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.common.util.MD5;
import com.atguigu.model.system.SysPost;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.SysPostQueryVo;
import com.atguigu.model.vo.SysUserQueryVo;
import com.atguigu.system.annotation.Log;
import com.atguigu.system.enums.BusinessType;
import com.atguigu.system.service.SysPostService;
import com.atguigu.system.service.SysUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags="岗位管理")
@RestController
@RequestMapping("/admin/system/sysPost")
public class SysPostController {
    @Autowired
    private SysPostService sysPostService;
    /**
     * 分页及带条件查询
     * @PathVariable注解 若方法参数名称和需要绑定的url中变量名称一致时,可以简写
     * @return
     */
    @ApiOperation("分页及带条件查询")
    @GetMapping("/{current}/{size}")
    @PreAuthorize("hasAuthority('bnt.sysPost.list')")
    public Result findPage(@ApiParam(name = "current",value = "当前页码",required = true)
                           @PathVariable Long current,
                           @ApiParam(name="size",value = "每条记录数",required = true)
                           @PathVariable Long size,
                           @ApiParam(name = "postQueryVo",value = "查询条件[查询对象]",required = false)
                                   SysPostQueryVo postQueryVo){
        System.out.println("postQueryVo = " + postQueryVo);
        System.out.println("postQueryVo = " + postQueryVo.getStatus());
        Page<SysPost> page = new Page<SysPost>(current,size);
        IPage<SysPost> pageModel = sysPostService.findPage(page, postQueryVo);
        return Result.ok(pageModel);
    }
    @ApiOperation("更改岗位状态")
    @GetMapping("/updateStatus/{postId}/{status}")
    public Result updateStatus(@PathVariable Long postId,@PathVariable Integer status){
        sysPostService.updatePostStatus(postId,status);
        return Result.ok();
    }
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @ApiOperation("添加岗位")
    @PreAuthorize("hasAuthority('bnt.sysPost.add')")
    @PostMapping("/save")
    public Result addPost(@RequestBody SysPost sysPost){
        sysPostService.save(sysPost);
        return Result.ok();
    }

    @ApiOperation("根据ID查询岗位")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id){
        System.out.println("id = " + id);
        SysPost sysPost = sysPostService.getById(id);
        return Result.ok(sysPost);
    }

    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @ApiOperation("更新岗位")
    @PreAuthorize("hasAuthority('bnt.sysPost.update')")
    @PutMapping("/update")
    public Result updatePost(@RequestBody SysPost sysPost){
        sysPostService.updateById(sysPost);
        return Result.ok();
    }

    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @ApiOperation("【逻辑】删除岗位")
    @PreAuthorize("hasAuthority('bnt.sysPost.remove')")
    @DeleteMapping("/remove/{id}")
    public Result deleteById(@PathVariable Long id){
        sysPostService.removeById(id);
        return Result.ok();
    }
}
