package com.nbd.ocp.core.jpa.crud.entity;



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

import com.nbd.ocp.common.repository.crud.IOcpCrudBaseDo;
import com.nbd.ocp.common.repository.multiTenancy.discriminator.entity.AbstractOcpTenancyEntity;
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
import java.time.Instant;

/**
 * @author jhb
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name="nbd_user_tenancy")
public class OcpUserDo  extends AbstractOcpTenancyEntity  implements IOcpCrudBaseDo {
    @Id
    @GenericGenerator(name="system-uuid", strategy = "uuid.hex")
    @GeneratedValue(generator="system-uuid")
    @Column(name="id",length = 32)
    private String id;

    @Column(name="user_name")
    private String userName;
    @Column(name="user_code")
    private String userCode;
    @Column
    private String phone;
    @Column
    private String email;

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

    @Column
    private String country;
    @Column
    private String province;
    @Column
    private String city;
    @Column
    private String language;
    @Column
    private Integer sex;
    @Column
    private String salt;
    @Column
    private String password;
    @Column
    private String portrait;

    @Column
    private Boolean locked;
    @Column(name="account_enabled")
    private Boolean accountEnabled;
    @Column(name="credentials_enabled")
    private Boolean credentialsEnabled;
    @Column
    private Boolean expired;




}
