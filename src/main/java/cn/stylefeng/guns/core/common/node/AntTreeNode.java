package cn.stylefeng.guns.core.common.node;


/**
 * 给ant design pro 树用的节点
 *
 * @author yaoliguo
 * @date 2019-05-11 09:46
 * ©2018 版权所有
 */
public class AntTreeNode {

    /**
     * 节点id
     */
    private Long key;

    private Long id;

    /**
     * 父节点id
     */
    private Long pId;

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
        antTreeNode.setId(0l);
        antTreeNode.setChecked(true);
        antTreeNode.setKey(0L);
        antTreeNode.setTitle("顶级");
        antTreeNode.setValue("top_menu");
        antTreeNode.setOpen(true);
        antTreeNode.setpId(0l);
        return antTreeNode;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getIconSkin() {
        return iconSkin;
    }

    public void setIconSkin(String iconSkin) {
        this.iconSkin = iconSkin;
    }
}
