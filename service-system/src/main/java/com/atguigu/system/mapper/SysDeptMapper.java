package com.atguigu.system.mapper;

import com.atguigu.model.system.SysDept;
import com.atguigu.model.system.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDeptMapper extends BaseMapper<SysDept> {
//    List<SysMenu> selectUserMenuListByUserId(Long deptId);
}
