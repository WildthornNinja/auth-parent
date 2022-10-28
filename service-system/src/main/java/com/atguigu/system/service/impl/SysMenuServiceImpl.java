package com.atguigu.system.service.impl;

import com.atguigu.common.helper.MenuHelper;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRoleMenu;
import com.atguigu.model.vo.AssginMenuVo;
import com.atguigu.system.exception.DiyException;
import com.atguigu.system.mapper.SysMenuMapper;
import com.atguigu.system.mapper.SysRoleMenuMapper;
import com.atguigu.system.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> findMenuNodes() {
        //调用SysMenuMapper 中 查询书所有记录的方法
        List<SysMenu> sysMenus = sysMenuMapper.selectList(null);
        //通过MenuHelper工具类 将所有的菜单转换为菜单树
        List<SysMenu> menuTree = MenuHelper.buildTree(sysMenus);
        return menuTree;
    }

    /**
     * 删除节点
     * @param id
     */
    @Override
    public void deleteEmptyNode(Long id) {
        //根据id 判断当前节点是否有子节点
        Integer count = sysMenuMapper.selectCount(new QueryWrapper<SysMenu>().eq("parent_id", id));
        if(count > 0){
            //该节点有子节点 ，不能删除 ，抛出自定义异常
            throw new DiyException(ResultCodeEnum.NODE_ERROR);
        }else{
            sysMenuMapper.deleteById(id);
        }
    }

    /**
     * 根据角色获取菜单数据
     * @param roleId
     * @return
     */
    @Override
    public List<SysMenu> getMenuByRoleId(Long roleId) {
        //现获取所有的status为1的菜单列表数据
        List<SysMenu> sysMenuList = sysMenuMapper.selectList(new QueryWrapper<SysMenu>().eq("status",1));
        //通过角色id 查询中间表 sys_role_menu ， 获取该角色已经拥有的菜单数据
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuMapper.selectList(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId));
        //通过遍历SysRoleMenuList，抽取出所有的菜单id 封装成List
        List<Long> roleMenuIds = new ArrayList<>();
        for (SysRoleMenu sysRoleMenu : sysRoleMenuList) {
            roleMenuIds.add(sysRoleMenu.getMenuId());
        }
        //遍历所有的菜单集合将该角色所拥有的菜单 select属性设为true 表示已被选中
        for (SysMenu sysMenu : sysMenuList) {
            if(roleMenuIds.contains(sysMenu.getId())){
                //设置该菜单【权限】已被分配
                sysMenu.setSelect(true);
            }else{
                sysMenu.setSelect(false);
            }
        }
        //将权菜单/限列表转换为权限树
        List<SysMenu> sysMenuTree = MenuHelper.buildTree(sysMenuList);
        return sysMenuTree;
    }

    /**
     * 给角色分配权限 【分配所属菜单】
     * @param assginMenuVo
     */
    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        //先删除该角色已经分配 所拥有的菜单 【在sys_role_menu中间表中删除菜单id】
        sysRoleMenuMapper.delete(new QueryWrapper<SysRoleMenu>().eq("role_id",assginMenuVo.getRoleId()));
        ////遍历新的 所有已选择的菜单/权限id
        for (Long menuId : assginMenuVo.getMenuIdList()) {
            //判空
            if(menuId!=null){
                //创建SysRoleMenu对象
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
                sysRoleMenu.setMenuId(menuId);
                //向数据库中新增数据 ，添加该角色的新权限
                sysRoleMenuMapper.insert(sysRoleMenu);
            }
        }

    }
}
