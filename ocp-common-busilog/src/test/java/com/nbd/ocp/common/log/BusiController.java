package com.nbd.ocp.common.log;
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


import com.nbd.ocp.core.exception.system.ExistsDataException;
import com.nbd.ocp.core.response.OcpJsonResponse;
import com.nbd.ocp.common.log.anotation.LogConfig;
import org.springframework.stereotype.Service;

/**
 * @author jin
 */

@Service
public class BusiController {

    @LogConfig(busiCode="1",operationCode="2")
    public OcpJsonResponse test(){
        return OcpJsonResponse.success();
    }

    @LogConfig(busiCode="1",operationCode="2")
    public OcpJsonResponse testError(){
        throw new ExistsDataException("sdf");
    }
}
