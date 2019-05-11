package cn.stylefeng.guns.core.common.node;

import lombok.Data;

/**
 * 给ant design pro 树用的节点
 *
 * @author yaoliguo
 * @date 2019-05-11 09:46
 * ©2018 版权所有
 */
@Data
public class AntTreeNode {

    /**
     * 节点id
     */
    private Long key;

    /**
     * 父节点id
     */
    private Long pKey;

    /**
     * 菜单编码
     */
    private String value;

    /**
     * 节点名称
     */
    private String title;

    /**
     * 是否打开节点
     */
    private Boolean open;

    /**
     * 是否被选中
     */
    private Boolean checked;

    /**
     * 节点图标  single or group
     */
    private String iconSkin;

    /**
     * 创建AntTreeNode的父级节点
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:51 PM
     */
    public static AntTreeNode createParent() {
        AntTreeNode antTreeNode = new AntTreeNode();
        antTreeNode.setChecked(true);
        antTreeNode.setKey(0L);
        antTreeNode.setTitle("顶级");
        antTreeNode.setOpen(true);
        antTreeNode.setPKey(0L);
        return antTreeNode;
    }

}
