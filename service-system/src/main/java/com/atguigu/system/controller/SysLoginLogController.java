package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysLoginLog;
import com.atguigu.model.vo.SysLoginLogQueryVo;
import com.atguigu.system.service.SysLoginLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "登陆日志管理")
@RestController
@RequestMapping("/admin/system/sysLoginLog")
public class SysLoginLogController {
    @Autowired
    private SysLoginLogService sysLoginLogService;
    /**
     * 分页及带条件查询
     * @PathVariable注解 若方法参数名称和需要绑定的url中变量名称一致时,可以简写
     * @return
     */
    @ApiOperation("分页及带条件查询")
    @RequestMapping("/{current}/{size}")
    public Result findPage(
            @ApiParam(name = "current",value = "当前页码",required = true)
            @PathVariable Long current,
            @ApiParam(name = "size",value = "每条记录数",required = true)
            @PathVariable Long size,
            @ApiParam(name = "sysLoginLogQueryVo",value = "查询条件[查询对象]",required = false)
            SysLoginLogQueryVo sysLoginLogQueryVo
    ){
        Page<SysLoginLog> page = new Page<>(current,size);
        IPage<SysLoginLog> pageMole =  sysLoginLogService.findPage(page,sysLoginLogQueryVo);
        return Result.ok(pageMole);
    }
}
