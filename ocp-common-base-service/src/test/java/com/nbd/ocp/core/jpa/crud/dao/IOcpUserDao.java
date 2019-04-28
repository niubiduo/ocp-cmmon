package com.nbd.ocp.core.jpa.crud.dao;
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


import com.nbd.ocp.core.jpa.crud.entity.OcpUserDo;
import com.nbd.ocp.common.repository.crud.IOcpCrudBaseDao;
import com.nbd.ocp.common.repository.multiTenancy.discriminator.annotations.OcpMultiTenancy;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * @author jin
 */
@OcpMultiTenancy
public interface IOcpUserDao extends IOcpCrudBaseDao<OcpUserDo, String> {
    @Query(value = "select * from nbd_user_tenancy ",nativeQuery = true)
    List<OcpUserDo> listUsers();
}
