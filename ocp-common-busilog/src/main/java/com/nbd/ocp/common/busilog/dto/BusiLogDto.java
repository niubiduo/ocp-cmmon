package com.nbd.ocp.common.busilog.dto;
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


import com.nbd.ocp.core.response.OcpJsonContract;
import com.nbd.ocp.core.response.OcpJsonResponse;
import com.nbd.ocp.common.busilog.anotation.LogConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.time.Instant;

/**
 * @author jin
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
public class BusiLogDto{

    private String busiCode;

    private String logTypeCode;

    private String operateCode;

    private Instant operateTime;


    private int operateStatus;

    private String operateParam;

    private String operateResult;


    private String clientIp;

    private String operatorUserId;

    private String operatorUserCode;

    private String tenantId;

    private String sysId;






    public static BusiLogDto of(LogConfig logConfig, Object retValue, Throwable error) {


        BusiLogDto busiLogDto=new BusiLogDto();
        busiLogDto.setBusiCode(logConfig.busiCode());
        busiLogDto.setOperateCode(logConfig.operationCode());
        busiLogDto.setLogTypeCode(logConfig.logTypeCode());
        busiLogDto.setOperateTime(Instant.now());
        if(error==null){
            if(retValue!=null && retValue instanceof OcpJsonResponse){
                OcpJsonResponse jsonResponse= (OcpJsonResponse) retValue;
                busiLogDto.setOperateStatus(jsonResponse.getStatus());
            }else{
                busiLogDto.setOperateStatus(OcpJsonContract.SUCCESS_STATUS);
            }
        }else{
            busiLogDto.setOperateStatus(OcpJsonContract.FAILED_STATUS);
        }
        return busiLogDto;
    }
    public boolean checkAvailable(){
        if(StringUtils.isEmpty(busiCode)){
            return false;
        }
        if(StringUtils.isEmpty(operateCode)){
            return false;
        }
        return true;
    }

}
