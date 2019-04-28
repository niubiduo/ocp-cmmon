package com.nbd.ocp.core.jpa.tree.service.impl;
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


import com.nbd.ocp.core.jpa.tree.dao.IOcpMenuDao;
import com.nbd.ocp.core.jpa.tree.entity.OcpMenuDo;
import com.nbd.ocp.core.jpa.tree.service.IOcpMenuService;
import com.nbd.ocp.common.repository.multiTenancy.discriminator.annotations.OcpMultiTenancy;
import com.nbd.ocp.common.repository.tree.IOcpTreeBaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jin
 */

@Service
@org.springframework.transaction.annotation.Transactional(readOnly = true,rollbackFor = Exception.class)
@OcpMultiTenancy
public class OcpMenuServiceImpl implements IOcpMenuService, IOcpTreeBaseServiceImpl<OcpMenuDo, IOcpMenuDao>{

    @Override
    public String getInnerCodeKey() {
        return "tree.menu";
    }
}
