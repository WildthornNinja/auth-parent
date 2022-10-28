package com.atguigu.system.service;

import com.atguigu.model.system.SysOperLog;
import com.atguigu.model.vo.SysOperLogQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysOperLogService extends IService<SysOperLog> {
    /**
     * 分页及带条件查询
     */
    IPage<SysOperLog> findPage(Page<SysOperLog> page, SysOperLogQueryVo sysOperLogQueryVo);
}
