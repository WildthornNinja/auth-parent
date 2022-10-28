package com.atguigu.system.service;

        import com.atguigu.model.system.SysMenu;
        import com.atguigu.model.vo.AssginMenuVo;
        import com.baomidou.mybatisplus.extension.service.IService;

        import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    /**
     * 菜单树形数据
     * @return
     */
    List<SysMenu> findMenuNodes();

    void deleteEmptyNode(Long id);

    /**
     * 根据角色获取菜单数据
     * @param roleId
     * @return
     */
    List<SysMenu> getMenuByRoleId(Long roleId);

    /**
     * 给角色分配权限 【分配所属菜单】
     * @param assginMenuVo
     */
    void doAssign(AssginMenuVo assginMenuVo);
}
