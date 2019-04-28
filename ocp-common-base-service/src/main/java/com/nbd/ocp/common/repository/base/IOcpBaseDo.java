package com.nbd.ocp.common.repository.base;
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


import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 基本do 平台和业务所有的do都要继承于此do
 * @author jhb
 */
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface IOcpBaseDo extends Serializable {

    /**
     * id
     *     @Id
     *     @GenericGenerator(name="system-uuid", strategy = "uuid.hex")
     *     @GeneratedValue(generator="system-uuid")
     *     @Column(name="id",length = 32)
     * @return id
     */
    String getId();


    /**
     * 系统id
     * @Column(name="sys_id")
     * @return 系统id
     */
    String getSysId();
    void setSysId(String sysId);

}
