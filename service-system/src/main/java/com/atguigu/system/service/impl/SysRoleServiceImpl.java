package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.model.vo.AssginRoleVo;
import com.atguigu.model.vo.SysRoleQueryVo;
import com.atguigu.system.mapper.SysRoleMapper;
import com.atguigu.system.mapper.SysUserRoleMapper;
import com.atguigu.system.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Override
    public IPage<SysRole> findPage(Page<SysRole> page, SysRoleQueryVo roleQueryVo) {
        return sysRoleMapper.findPage(page,roleQueryVo);
    }
    /**
     * 根据用户id 在 sys_user_role表中获取角色数据 返回角色id集合 和 用户id集合
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> getRolesByUserId(Long userId) {
        //获取所有角色数据 [在sys_role 表中获取]
        //selectList() 参数为null ，代表无查询条件
        List<SysRole> sysRoleList = sysRoleMapper.selectList(null);
        //通过用户id 查询用户语句分配的角色id [在sys_user_role 中间表中获取已分配的角色id]
        List<SysUserRole> sysUserRoleList = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        //返回值为 SysUserRole 对象 ，我们只需要其中的roleId值
        List<Long> userRoleIds = new ArrayList<>();
        //遍历sysUserRoleList集合 ，获取roleId值
        for (SysUserRole sysUserRole : sysUserRoleList) {
            userRoleIds.add(sysUserRole.getRoleId());
        }
        //创建返回Map 集合
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("allRoles",sysRoleList);
        returnMap.put("userRoleIds",userRoleIds);
        return returnMap;
    }

    /**
     * 按照用户id，给其分配角色
     * @param assginRoleVo
     */
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        //先删除该用户目前的所有已分配角色
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().eq("user_id",assginRoleVo.getUserId()));
        //遍历AssginRoleVo对象的 roleIdList属性 ，循环插入数据库表
        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        System.out.println("roleIdList = " + roleIdList);
        for (Long roleId : roleIdList) {
            //进行roleId判空
            if(roleId !=null){
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(assginRoleVo.getUserId());
                sysUserRole.setRoleId(roleId);
                //保存
                sysUserRoleMapper.insert(sysUserRole);
            }
        }
    }
}
