package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysOperLog;
import com.atguigu.model.vo.SysOperLogQueryVo;
import com.atguigu.model.vo.SysRoleQueryVo;
import com.atguigu.system.service.SysOperLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "操作日常管理")
@RestController
@RequestMapping("/admin/system/sysOperLog")
public class SysOperLogController {
    @Autowired
    private SysOperLogService sysOperLogService;

    /**
     * 分页及带条件查询
     */
    @ApiOperation("分页及带条件查询")
    @GetMapping("/{current}/{size}")
    public Result findPage(
            @ApiParam(name = "current",value = "当前页码",required = true)
            @PathVariable Long current,
            @ApiParam(name = "size",value = "每条记录数",required = true)
            @PathVariable Long size,
            @ApiParam(name = "sysOperLogQueryVo",value = "查询条件[查询对象]",required = false)
                    SysOperLogQueryVo sysOperLogQueryVo){
        Page<SysOperLog> page = new Page<>(current,size);
        IPage<SysOperLog> pageModel = sysOperLogService.findPage(page,sysOperLogQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("通过id查询详情")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id){
        SysOperLog sysOperLog = sysOperLogService.getById(id);
        return Result.ok(sysOperLog);

    }
}
