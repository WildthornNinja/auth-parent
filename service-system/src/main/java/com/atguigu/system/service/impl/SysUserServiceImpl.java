package com.atguigu.system.service.impl;

import com.atguigu.common.helper.MenuHelper;
import com.atguigu.common.helper.RouterHelper;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.RouterVo;
import com.atguigu.model.vo.SysUserQueryVo;
import com.atguigu.system.mapper.SysMenuMapper;
import com.atguigu.system.mapper.SysRoleMapper;
import com.atguigu.system.mapper.SysUserMapper;
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
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Override
    public IPage<SysUser> findPage(Page<SysUser> page, SysUserQueryVo userQueryVo) {
        return sysUserMapper.findPage(page,userQueryVo);
    }

    /**
     * 更新用户状态
     * @param userId
     * @param status
     */
    @Override
    public void updateUserStatus(Long userId, Integer status) {
        //通过id查询user信息 ，然后更新器状态信息
        System.out.println("userId = " + userId);
        SysUser sysUser = sysUserMapper.selectById(userId);
        System.out.println("sysUser = " + sysUser);
        sysUser.setStatus(status);
        sysUser.setUpdateTime(new Date());
        //更新用户状态
        sysUserMapper.updateById(sysUser);
    }

    /**
     * 登陆查询
     * @param username
     * @return
     */
    @Override
    public SysUser getSysUserByUserName(String username) {
        return sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username",username));
    }

    /**
     * 获取用户登录信息
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> getUserInfoById(Long userId) {
        //根据用户id查询用户信息
        SysUser sysUser = sysUserMapper.selectById(userId);
        //调用当前实现类中的根据用户id获取权限菜单的方法
        List<SysMenu> userMenuList = getUserMenusByUserId(userId);
        //将userMenuList转换为菜单树
        List<SysMenu> userMenuTree = MenuHelper.buildTree(userMenuList);
        //获取用户的权限菜单，并将其转换为Router路由，放在map中
        //通过RouterHelper工具栏将菜单树转换为路由
        List<RouterVo> userRouters = RouterHelper.buildRouters(userMenuTree);
        //根据用户id获取用户的按钮权限标识符
        List<String> userBtnPerms = getUserBtnPermsByUserId(userId);

        Map<String,Object> map = new HashMap<>();
        map.put("avatar",sysUser.getHeadUrl());
        map.put("name",sysUser.getName());
        //获取用户的按钮权限标识符
        map.put("buttons", userBtnPerms);
        //获取用户的权限菜单并将其转换为router路由，并放在map中
        map.put("routers", userRouters);

        return map;
    }

    /**
     * 根据用户id 查询sys_menu表获取按钮权限 perms
     * @param userId
     * @return
     */
    @Override
    public List<String> getUserBtnPermsByUserId(Long userId) {
        //创建一个返回的List
        List<String> btnPermsList = new ArrayList<>();
        //获取用户的权限菜单
        List<SysMenu> userMenusByUserId = getUserMenusByUserId(userId);
        //遍历userMenusByUserId集合 ，获取按钮权限
        for (SysMenu sysMenu : userMenusByUserId) {
            //只获取按钮权限，按钮type为2
            if(sysMenu.getType()==2){
                //获取按钮的权限标识符
                btnPermsList.add(sysMenu.getPerms());
            }
        }
        return btnPermsList;
    }

    /**
     *根据用户id查询用户的权限菜单
     */
    public List<SysMenu> getUserMenusByUserId(Long userId){
        List<SysMenu> userMenuList = null;
        //判断该用户是否是系统管理员
        if(userId ==1L){
            //证明该用户是管理员
            userMenuList = sysMenuMapper.selectList(new QueryWrapper<SysMenu>().eq("status",1).orderByAsc("sort_value"));

        }else{
            //根据用户id查询用户的权限菜单
            userMenuList = sysMenuMapper.selectUserMenuListByUserId(userId);
        }
        return userMenuList;
    }

}
