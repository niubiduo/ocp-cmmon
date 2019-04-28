package com.nbd.ocp.common.repository.tree;
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


import com.nbd.ocp.common.repository.base.IOcpBaseDo;

import javax.persistence.MappedSuperclass;
import java.util.List;

/**
 * @author jhb
 */
@MappedSuperclass
public  interface IOcpTreeBaseDo extends IOcpBaseDo {


    /**
     * @Column(name="pid")
     * @return 父类id
     */
    String getPid();

    void setPid(String pid);

    /**
     * 父节点名称
     * @return
     */
    String getPTitle();

    void setPTitle(String ptitle);

    /**
     * select * from nbd_role where inner_code = CAST('00000z' AS BINARY(6))
     * @Column(name="inner_code",nullable=false,unique = true,columnDefinition = "varbinary(6) NOT NULL ")
     * @return 返回innerCode
     */
    String getInnerCode();

    void setInnerCode(String innerCode);

    /**
     * @Column(name="cascade_inner_code",nullable=false,unique = true,columnDefinition = "varbinary(240) NOT NULL ")
     * @return
     */
    String getCascadeInnerCode();

    void setCascadeInnerCode(String cascadeInnerCode);

    <T extends  IOcpTreeBaseDo> List<T> getChildren();

    String getTitle();

}
