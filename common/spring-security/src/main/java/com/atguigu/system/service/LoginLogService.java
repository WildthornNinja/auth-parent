package com.atguigu.system.service;

public interface LoginLogService {
    //记录日志
    void recordLoginLog(String username, String ipAddr , Integer status , String msg);
}
