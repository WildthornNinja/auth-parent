package com.atguigu.system.test;

import com.atguigu.model.system.SysRole;
import com.atguigu.system.mapper.SysRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SysRoleMapperTest {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 测试查询所有
     */
    @Test
    public void testSelectList(){
        //调用SelectList方法， 无查询条件可以传入 null值
        List<SysRole> sysRoleList = sysRoleMapper.selectList(null);
        for (SysRole sysRole : sysRoleList) {
            System.out.println("sysRole = " + sysRole);
        }
    }
    /**
     * 测试添加
     */
    @Test
    public void testInsert(){
        SysRole sysRole = new SysRole("ikun","kun","唱跳rap篮球");
        int insert = sysRoleMapper.insert(sysRole);
        System.out.println("insert = " + insert);
        //Mybatis-plus可以将自动生成的id返回
        //sysRole.getId();
    }
    /**
     * 测试更新
     */
    @Test
    public void testUpdate(){
        SysRole sysRole = sysRoleMapper.selectById(9);
        sysRole.setRoleName("ikun_new");
        sysRole.setDescription("小黑子");
        //更新
        sysRoleMapper.updateById(sysRole);
    }
    /**
     * 测试删除
     */
    @Test
    public void testDelete(){
        //删除id为9的记录 ，逻辑删除
        int i = sysRoleMapper.deleteById(9);
        System.out.println(i>0?"删除成功":"删除失败");
    }
    /**
     * 测试批量查询
     */
    @Test
    public void testBatchSelect(){
        //创建一个保存要查询记录的id集合
        List<Long> idList = new ArrayList<>();
        idList.add(2L);
        idList.add(8L);
        idList.add(9L);
        List<SysRole> sysRoleList = sysRoleMapper.selectBatchIds(idList);
        for (SysRole sysRole : sysRoleList) {
            System.out.println("sysRole = " + sysRole);
        }
    }
    /**
     * 测试批量删除
     * application-dev.yml 加入配置
     * 此为默认值，如果你的默认值和默认的一样，则不需要该配置
     * mybatis-plus:
     *   global-config:
     *     db-config:
     *       logic-delete-value: 1
     *       logic-not-delete-value: 0
     */
    @Test
    public void testBatchDelete(){
        //创建一个保存要删除记录的id集合
        List<Long> idList = new ArrayList<>();
        idList.add(10L);
        idList.add(11L);
        idList.add(12L);
        int i = sysRoleMapper.deleteBatchIds(idList);
        System.out.println(i>0?"删除成功":"删除失败");
    }
    /**
     * 获取总的记录数
     */
    @Test
    public void testSelectCount(){
        Integer integer = sysRoleMapper.selectCount(null);
        System.out.println("integer = " + integer);
    }
    /**
     * 测试带条件的查询
     */
    @Test
    public void testQueryWrapper(){
        //创建QueryWrapper 对象
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        //查询 role_name 中 包含 "员" 的记录
        // like 两边加 %
        // likeRight 右边加 %
        // likeLeft 左边加 %
        //可以定义只查询的具体列
        queryWrapper.like("role_name","员").select("id","role_name");
        List<SysRole> sysRoleList = sysRoleMapper.selectList(queryWrapper);
        for (SysRole sysRole : sysRoleList) {
            System.out.println("sysRole = " + sysRole);
        }
    }
    /**
     * 测试带条件的查询
     */
    @Test
    public void testQueryWrapper1(){
        //创建QueryWrapper 对象
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        //查询 role_name 中 包含 "员" 的记录
        // like 两边加 %
        // likeRight 右边加 %
        // likeLeft 左边加 %
        //可以定义只查询的具体列
        queryWrapper.like("role_name","员");
        List<SysRole> sysRoleList = sysRoleMapper.selectList(queryWrapper);
        for (SysRole sysRole : sysRoleList) {
            System.out.println("sysRole = " + sysRole);
        }
    }
    /**
     * 测试带条件的查询
     */
    @Test
    public void testQueryWrapper2(){
        //创建QueryWrapper 对象
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        //查询 role_name 中 以 "测试" 开头的记录
        queryWrapper.likeRight("role_name","测试").select("id","role_name");
        List<SysRole> sysRoleList = sysRoleMapper.selectList(queryWrapper);
        for (SysRole sysRole : sysRoleList) {
            System.out.println("sysRole = " + sysRole);
        }
    }
    /**
     * 测试带条件的查询
     */
    @Test
    public void testQueryWrapper3(){
        //创建QueryWrapper 对象
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        //查询 role_name 中 以 "员" 结尾的记录 按照id降序 排序
        //可以定义只查询的具体列
        queryWrapper.like("role_name","员").select("id","role_name").orderByDesc("id");
        List<SysRole> sysRoleList = sysRoleMapper.selectList(queryWrapper);
        for (SysRole sysRole : sysRoleList) {
            System.out.println("sysRole = " + sysRole);
        }
    }
    /**
     * 测试局部更新
     */
    @Test
    public void testUpdateWrapper(){
        //创建UpdateWrapper 对象
        UpdateWrapper<SysRole> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("description","负责开发工作").eq("id",12);
        int update = sysRoleMapper.update(null,updateWrapper);
        System.out.println("update = " + update);
    }

    /**
     * 测试分页
     */
    @Test
    public void testSelectPage(){
        //创建Page对象
        //current 参数 ，当前第几页  size ，一页显示的数据条数
        Page<SysRole> page = new Page<>(2,3);
        Page<SysRole> sysRolePage = sysRoleMapper.selectPage(page, null);
        //获取当前页
        long current = sysRolePage.getCurrent();
        System.out.println("当前页是 = " + current);
        //获取每页显示的条数
        long size = sysRolePage.getSize();
        System.out.println("每页显示的条数 = " + size);
        //获取总记录数
        long total = sysRolePage.getTotal();
        System.out.println("总记录数 = " + total);
        //获取总页数
        long pages = sysRolePage.getPages();
        System.out.println("总页数 = " + pages);
        //是否有上一页
        boolean hasNext = sysRolePage.hasNext();
        System.out.println("是否有上一页 = " + hasNext);
        //是否有下一页
        boolean hasPrevious = sysRolePage.hasPrevious();
        System.out.println("是否有下一页 = " + hasPrevious);
        //获取当前页数据
        List<SysRole> records = sysRolePage.getRecords();
        System.out.println("当前页数据 = " + records);
        for (SysRole record : records) {
            System.out.println("record = " + record);
        }
    }
}
