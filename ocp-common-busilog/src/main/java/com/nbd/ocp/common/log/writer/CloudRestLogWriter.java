package com.nbd.ocp.common.log.writer;
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


import com.nbd.ocp.core.sdk.AbstractBaseSdk;
import com.nbd.ocp.core.response.OcpJsonResponse;
import com.nbd.ocp.common.log.dto.BusiLogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author jin
 */

@Service
public class CloudRestLogWriter extends AbstractBaseSdk implements ILogWriter{

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Async
    public void write(BusiLogDto busiLogDto) {
        restTemplate.postForEntity("http://sc-services-busilog/busi-log",busiLogDto, OcpJsonResponse.class);
    }
}
