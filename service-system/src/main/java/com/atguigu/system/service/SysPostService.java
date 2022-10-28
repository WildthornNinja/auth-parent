package com.atguigu.system.service;

import com.atguigu.model.system.SysPost;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.SysPostQueryVo;
import com.atguigu.model.vo.SysUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


public interface SysPostService extends IService<SysPost> {
    public IPage<SysPost> findPage(Page<SysPost> page, SysPostQueryVo postQueryVo);
    void updatePostStatus(Long postId, Integer status);
}
