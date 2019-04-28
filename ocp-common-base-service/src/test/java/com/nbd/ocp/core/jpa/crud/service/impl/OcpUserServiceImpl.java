package com.nbd.ocp.core.jpa.crud.service.impl;
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


import com.nbd.ocp.core.jpa.crud.dao.IOcpUserDao;
import com.nbd.ocp.core.jpa.crud.entity.OcpUserDo;
import com.nbd.ocp.core.jpa.crud.service.IOcpUserService;
import com.nbd.ocp.common.repository.crud.IOcpCrudBaseServiceImpl;
import com.nbd.ocp.common.repository.multiTenancy.discriminator.annotations.OcpMultiTenancy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author jin
 */
@Service
@OcpMultiTenancy
@org.springframework.transaction.annotation.Transactional(readOnly = true,rollbackFor = Exception.class)
public class OcpUserServiceImpl implements IOcpUserService, IOcpCrudBaseServiceImpl<OcpUserDo, IOcpUserDao> {
    @Autowired
    IOcpUserDao userDao;

    @Override
    public List<OcpUserDo> findAll() {
        return userDao.findAll();
    }

    @Override
    @Transactional
    public OcpUserDo save(OcpUserDo userDO) {
        return userDao.save(userDO);
    }

    @Override
    public List<OcpUserDo> listUsers() {
        return userDao.listUsers();
    }


}
