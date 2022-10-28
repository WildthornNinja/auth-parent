package com.atguigu.common.helper;


import com.atguigu.model.system.SysDept;
import com.atguigu.model.system.SysDept;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 根据权限数据构建菜单数据
 * </p>
 *
 */
public class DeptHelper {

    //把一个List转成树
    public static List<SysDept> buildTree(List<SysDept> list, Long parentId) {
        List<SysDept> tree = new ArrayList<>();
        for (SysDept org : list) {
            if (Objects.equals(org.getParentId(), parentId)) {
                tree.add(findChild(org, list));
            }
        }
        return tree;
    }

    private static SysDept findChild(SysDept org, List<SysDept> list) {
        for (SysDept n : list) {
            if (Objects.equals(n.getParentId(), org.getId())) {
                if (org.getChildren() == null) {
                    org.setChildren(new ArrayList<SysDept>());
                }
                org.getChildren().add(findChild(n, list));
            }
        }
        return org;
    }


    /**
     * 使用递归方法建菜单
     * @param sysDeptList
     * @return
     */
    public static List<SysDept> buildTree(List<SysDept> sysDeptList) {
        List<SysDept> trees = new ArrayList<>();
        for (SysDept sysDept : sysDeptList) {
            if (sysDept.getParentId().longValue() == 0) {
                trees.add(findChildren(sysDept,sysDeptList));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
    public static SysDept findChildren(SysDept sysDept, List<SysDept> treeNodes) {
        sysDept.setChildren(new ArrayList<SysDept>());

        for (SysDept it : treeNodes) {
            if(sysDept.getId().longValue() == it.getParentId().longValue()) {
                if (sysDept.getChildren() == null) {
                    sysDept.setChildren(new ArrayList<>());
                }
                sysDept.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return sysDept;
    }
}
