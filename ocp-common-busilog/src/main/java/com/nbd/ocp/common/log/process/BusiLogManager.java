package com.nbd.ocp.common.log.process;
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


import com.nbd.ocp.core.exception.system.SysClassNotFoundException;
import com.nbd.ocp.common.log.anotation.LogBeanConfig;
import com.nbd.ocp.common.log.anotation.LogConfig;
import com.nbd.ocp.common.log.dto.BusiLogDto;
import com.nbd.ocp.common.log.util.AnnotationUtil;
import com.nbd.ocp.common.log.writer.ILogWriter;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jin
 */

@Component
public class BusiLogManager {
    private static final Logger logger= LoggerFactory.getLogger(BusiLogManager.class);

    @Autowired
    private List<ILogWriter> logWriterList;
    private static ConcurrentHashMap<String,ILogWriter> logWriterMap=new ConcurrentHashMap<>();

    public void log(JoinPoint joinPoint, Object retValue,  Throwable error) {
        LogConfig logConfig= AnnotationUtil.getMethodLogConfig(joinPoint, LogConfig.class);
        BusiLogDto busiLogDto=BusiLogDto.of(logConfig,retValue,error);
        if(StringUtils.isEmpty(busiLogDto.getBusiCode())){
            LogBeanConfig logBeanConfig=AnnotationUtil.getClassLogConfig(joinPoint, LogBeanConfig.class);
            if(logBeanConfig!=null){
                busiLogDto.setBusiCode(logBeanConfig.busiCode());
            }
        }
        if(busiLogDto.checkAvailable()){
            ILogWriter logWriter=getWriter(logConfig);
            logWriter.write(busiLogDto);
        }else{
            logger.error("业务日志未成功发送,日志：{}",busiLogDto);
        }
    }

    public ILogWriter getWriter(LogConfig logConfig){
        Class<? extends ILogWriter> clazzTarget=logConfig.writer();
        ILogWriter cacheLogWriter=logWriterMap.get(logConfig.writer().getName());
        if(cacheLogWriter!=null){
            return cacheLogWriter;
        }
        for(ILogWriter logWriter:logWriterList){
            Class clazzItem=AopUtils.getTargetClass(logWriter);
            if(clazzTarget.isAssignableFrom(clazzItem)){
                logWriterMap.put(logConfig.writer().getName(),logWriter);
                return logWriter;
            }
        }
        throw new SysClassNotFoundException("Cannot found "+clazzTarget.getName());
    }
}
