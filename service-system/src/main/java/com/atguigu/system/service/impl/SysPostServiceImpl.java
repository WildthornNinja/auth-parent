package com.atguigu.system.service.impl;

import com.atguigu.common.helper.MenuHelper;
import com.atguigu.common.helper.RouterHelper;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysPost;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.RouterVo;
import com.atguigu.model.vo.SysPostQueryVo;
import com.atguigu.model.vo.SysUserQueryVo;
import com.atguigu.system.mapper.SysMenuMapper;
import com.atguigu.system.mapper.SysPostMapper;
import com.atguigu.system.mapper.SysUserMapper;
import com.atguigu.system.service.SysPostService;
import com.atguigu.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {
    @Autowired
    private SysPostMapper sysPostMapper;

    @Override
    public IPage<SysPost> findPage(Page<SysPost> page, SysPostQueryVo postQueryVo) {
//        if(!postQueryVo.getStatus()){
//            postQueryVo
//        }
        return sysPostMapper.findPage(page,postQueryVo);
    }
    /**
     * 更新岗位状态
     * @param postId
     * @param status
     */
    @Override
    public void updatePostStatus(Long postId, Integer status) {
        //通过id查询post信息 ，然后更新状态信息
        SysPost sysPost = sysPostMapper.selectById(postId);
        sysPost.setStatus(status);
        sysPost.setUpdateTime(new Date());
        //更新状态
        sysPostMapper.updateById(sysPost);
    }


}
