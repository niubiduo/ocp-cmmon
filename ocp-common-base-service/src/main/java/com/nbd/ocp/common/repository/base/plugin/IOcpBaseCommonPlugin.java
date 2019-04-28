package com.nbd.ocp.common.repository.base.plugin;
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


import com.nbd.ocp.common.repository.base.IOcpBaseDao;
import com.nbd.ocp.common.repository.base.IOcpBaseDo;


/**
 * 使用baseService时使用插件扩展时，通用的顶层接口定义
 * @author jhb
 */
public interface IOcpBaseCommonPlugin<T extends IOcpBaseDo,I extends IOcpBaseDao> extends IOcpBasePlugin<T,I> {
    boolean  support(Class<?> c);
}
