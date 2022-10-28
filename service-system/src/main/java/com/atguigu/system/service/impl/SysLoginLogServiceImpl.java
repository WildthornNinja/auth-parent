package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysLoginLog;
import com.atguigu.model.vo.SysLoginLogQueryVo;
import com.atguigu.system.mapper.SysLoginLogMapper;
import com.atguigu.system.service.SysLoginLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {
    @Autowired
    private SysLoginLogMapper sysLoginLogMapper;
    /**
     * 分页及带条件查询
     * @param page
     * @param sysLoginLogQueryVo
     * @return
     */
    @Override
    public IPage<SysLoginLog> findPage(Page<SysLoginLog> page, SysLoginLogQueryVo sysLoginLogQueryVo) {
        return sysLoginLogMapper.findPage(page,sysLoginLogQueryVo);
    }
}
