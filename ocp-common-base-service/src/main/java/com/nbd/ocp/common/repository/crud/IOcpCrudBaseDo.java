package com.nbd.ocp.common.repository.crud;
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
import com.nbd.ocp.common.repository.base.IOcpBaseDo;

import javax.persistence.MappedSuperclass;

/**
 * 基本do 平台和业务所有的do都要继承于此do
 * @author jhb
 */
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface IOcpCrudBaseDo extends IOcpBaseDo {

}
