package com.atguigu.system.service;

import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.SysUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


public interface SysUserService extends IService<SysUser> {
    public IPage<SysUser> findPage(Page<SysUser> page, SysUserQueryVo userQueryVo);
    //实现 更新用户状态信息
    void updateUserStatus(Long userId, Integer status);

    SysUser getSysUserByUserName(String username);

    /**
     * 根据用户id获取用户登录信息
     * @param userId
     * @return
     */
    Map<String, Object> getUserInfoById(Long userId);

    /**
     * 根据用户id获取用户按钮权限标识符
     * @param userId
     * @return
     */
    List<String> getUserBtnPermsByUserId(Long userId);
}
