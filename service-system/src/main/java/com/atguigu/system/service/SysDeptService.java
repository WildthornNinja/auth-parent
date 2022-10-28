package com.atguigu.system.service;

import com.atguigu.model.system.SysDept;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.vo.AssginMenuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysDeptService extends IService<SysDept> {

    /**
     * 部门树形数据
     * @return
     */
    List<SysDept> findDeptNodes();

    void deleteEmptyNode(Long id);


    Object findUserNodes();
}
