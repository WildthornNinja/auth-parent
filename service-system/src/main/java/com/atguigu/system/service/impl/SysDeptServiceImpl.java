package com.atguigu.system.service.impl;

import com.atguigu.common.helper.DeptHelper;
import com.atguigu.common.helper.MenuHelper;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.model.system.SysDept;
import com.atguigu.model.system.SysDept;

import com.atguigu.system.exception.DiyException;
import com.atguigu.system.mapper.SysDeptMapper;
import com.atguigu.system.service.SysDeptService;
import com.atguigu.system.service.SysDeptService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.experimental.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Override
    public List<SysDept> findDeptNodes() {
        //调用SysDeptMapper 中 查询所有记录的方法
        List<SysDept> sysDepts = sysDeptMapper.selectList(null);
        //通过DeptHelper工具类 将所有的部门转换为部门树
        List<SysDept> menuTree = DeptHelper.buildTree(sysDepts);
        return menuTree;
    }

    /**
     * 删除节点
     * @param id
     */
    @Override
    public void deleteEmptyNode(Long id) {
        //根据id 判断当前节点是否有子节点
        Integer count = sysDeptMapper.selectCount(new QueryWrapper<SysDept>().eq("parent_id", id));
        if(count > 0){
            //该节点有子节点 ，不能删除 ，抛出自定义异常
            throw new DiyException(ResultCodeEnum.NODE_ERROR);
        }else{
            sysDeptMapper.deleteById(id);
        }
    }

    @Override
    public Object findUserNodes() {
        return null;
    }

//    /**
//     * 根据角色获取部门数据
//     * @param roleId
//     * @return
//     */
//    @Override
//    public List<SysDept> getDeptByRoleId(Long roleId) {
//        //现获取所有的status为1的部门列表数据
//        List<SysDept> sysDeptList = sysDeptMapper.selectList(new QueryWrapper<SysDept>().eq("status",1));
//        //通过角色id 查询中间表 sys_role_menu ， 获取该角色已经拥有的部门数据
//        List<SysRoleDept> sysRoleDeptList = sysRoleDeptMapper.selectList(new QueryWrapper<SysRoleDept>().eq("role_id", roleId));
//        //通过遍历SysRoleDeptList，抽取出所有的部门id 封装成List
//        List<Long> roleDeptIds = new ArrayList<>();
//        for (SysRoleDept sysRoleDept : sysRoleDeptList) {
//            roleDeptIds.add(sysRoleDept.getDeptId());
//        }
//        //遍历所有的部门集合将该角色所拥有的部门 select属性设为true 表示已被选中
//        for (SysDept sysDept : sysDeptList) {
//            if(roleDeptIds.contains(sysDept.getId())){
//                //设置该部门【权限】已被分配
//                sysDept.setSelect(true);
//            }else{
//                sysDept.setSelect(false);
//            }
//        }
//        //将权部门/限列表转换为权限树
//        List<SysDept> sysDeptTree = DeptHelper.buildTree(sysDeptList);
//        return sysDeptTree;
//    }
    

//    }
}
