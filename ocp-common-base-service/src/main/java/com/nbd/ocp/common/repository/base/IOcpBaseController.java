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


import com.nbd.ocp.core.context.util.OcpSpringUtil;
import com.nbd.ocp.common.repository.utils.OcpGenericsUtils;

/**
 * @author jin
 */

public interface IOcpBaseController<T extends IOcpBaseDo,I extends IOcpBaseService> {
    default I getBaseService(){
        return (I) OcpSpringUtil.getBean(OcpGenericsUtils.getControllerSuperClassGenericsType(getClass(), IOcpBaseService.class));
    }
}
