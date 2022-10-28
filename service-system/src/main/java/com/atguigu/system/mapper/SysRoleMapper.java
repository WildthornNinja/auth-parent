package com.atguigu.system.mapper;

import com.atguigu.model.system.SysRole;
import com.atguigu.model.vo.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {
    //分页及带条件查询的方法
    IPage<SysRole> findPage(Page<SysRole> page, @Param("vo")SysRoleQueryVo roleQueryVo);

    /**
     * 自定义分页 注意点
     *  语法 limit i , j
     *  i: 查询结果的索引值（默认从0开始）
     *  j: 查询结果返回数量;
     *  假设当前页是pageNo ,每页显示的条数是 size ，那么分页条件为 limit(pageNo-1)*size,size
     */
}
