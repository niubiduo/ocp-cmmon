package com.nbd.ocp.core.jpa.tree.entity;
/*
                       _ooOoo_
                      o8888888o
                      88" . "88
                      (| -_- |)
                      O\  =  /O
                   ____/`---'\____
                 .'  \\|     |//  `.
                /  \\|||  :  |||//  \
               /  _||||| -:- |||||-  \
               |   | \\\  -  /// |   |
               | \_|  ''\---/''  |   |
               \  .-\__  `-`  ___/-. /
             ___`. .'  /--.--\  `. . __
          ."" '<  `.___\_<|>_/___.'  >'"".
         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
         \  \ `-.   \_ __\ /__ _/   .-` /  /
    ======`-.____`-.___\_____/___.-`____.-'======
                       `=---='
    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
             佛祖保佑       永无BUG
*/


import com.nbd.ocp.common.repository.multiTenancy.discriminator.entity.AbstractOcpTenancyEntity;
import com.nbd.ocp.common.repository.tree.IOcpTreeBaseDo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jin
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name="nbd_menu")
public class OcpMenuDo extends AbstractOcpTenancyEntity implements IOcpTreeBaseDo {
    @Id
    @GenericGenerator(name="system-uuid", strategy = "uuid.hex")
    @GeneratedValue(generator="system-uuid")
    @Column(name="id",length = 32)
    private String id;

    @Column(name="version")
    private Integer version;

    @Column(name="status")
    private Integer status;

    @Column(name="ts")
    @UpdateTimestamp
    private Instant ts ;

    @CreationTimestamp
    @Column(name="create_time")
    private Instant createTime;

    @Column(name="creator")
    private String  creator;

    @Column(name="sys_id")
    private String sysId;

    @Column(name="tenant_id")
    private String tenantId;

    @Column(name="inner_code",nullable=false,unique = true,columnDefinition = "varbinary(6) NOT NULL ")
    private String  innerCode;

    @Column(name="cascade_inner_code",nullable=false,unique = true,columnDefinition = "varbinary(240) NOT NULL ")
    private String cascadeInnerCode;

    @Column(name="pid")
    private String pid;

    @Column(name="menu_code",nullable=false,unique = true)
    private String  menuCode;

    @Column(name="menu_name",nullable=false,unique = true)
    private String  menuName;


    /**
     * 图标
     */
    @Column
    private String  icon;

    /**
     * 类型
     */
    @Column(name="menu_type",nullable=false)
    private int  menuType;

    /**
     * 打开新窗口
     */
    @Column(name="blank",nullable=false)
    private boolean blank;

    /**
     * 路由/链接
     */
    @Column(name="path",nullable=false)
    private String  path;

    /**
     * 顺序
     */
    @Column(name="menu_order",nullable=false)
    private int  order;

    /**
     * 是否在菜单隐藏
     */
    @Column(nullable=false)
    private boolean  hideInMenu=false;

    @Transient
    private String pTitle;

    @Transient
    private boolean checked;

    @Transient
    private List<OcpMenuDo> children =new ArrayList<>();

    @Override
    public String getTitle() {
        return this.getMenuName();
    }


}
