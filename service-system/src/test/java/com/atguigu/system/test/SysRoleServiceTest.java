package com.atguigu.system.test;

import com.atguigu.model.system.SysRole;
import com.atguigu.system.service.SysRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SysRoleServiceTest {
    @Autowired
    private SysRoleService sysRoleService;
    /**
     * 测试查询所有
     */
    @Test
    public void testList(){
        List<SysRole> list = sysRoleService.list();
        for (SysRole sysRole : list) {
            System.out.println("sysRole = " + sysRole);
        }

    }
    /**
     * 测试新增
     */
    @Test
    public void testSave(){
        SysRole sysRole = new SysRole("销售人员","销售","负责销售工作");
        boolean save = sysRoleService.save(sysRole);
        System.out.println(save?"新增成功":"新增失败");

    }
    /**
     * 测试更新
     */
    @Test
    public void testUpdateById(){
        //通过id查询获取信息
        SysRole sysRole = sysRoleService.getById(13);
        sysRole.setRoleName("new-销售人员");
        boolean update = sysRoleService.updateById(sysRole);
        System.out.println(update?"修改成功":"修改失败");
    }

    /**
     * 测试删除
     */
    @Test
    public void testRemove(){
        sysRoleService.removeById(13);
    }
}
