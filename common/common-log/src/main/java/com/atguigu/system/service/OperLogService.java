package com.atguigu.system.service;

import com.atguigu.model.system.SysOperLog;

/**
 * 保存操作日志的接口
 */
public interface OperLogService {
    /**
     * 保存操作日志
     * @param sysOperLog
     */
    void saveSysOperLog(SysOperLog sysOperLog);
}
