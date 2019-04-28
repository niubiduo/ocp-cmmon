package com.nbd.ocp.common.repository.tree.utils;

import com.nbd.ocp.common.repository.tree.IOcpTreeBaseService;
import com.nbd.ocp.core.context.threadlocal.InvocationInfoProxy;
import com.nbd.ocp.core.context.util.OcpSpringUtil;
import com.nbd.ocp.core.utils.number.OcpNumberBaseConversionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.locks.ReentrantLock;

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


/**
 * @author jhb
 */
public class OcpInnerCodeUtils {
    private  static Logger logger = LoggerFactory.getLogger(OcpInnerCodeUtils.class);

    private static final  ReentrantLock lock =new ReentrantLock();

    private static String[] pre=new String[]{"00000","0000","000","00","0",""};


    private static final  String  INCR_KEY_POSTFIX="inner_code_queue";

    /**
     * 通过redis 自增序列获取long值，转为4位36位
     * @return
     */
    public static String getRedisKey(String innerCodeKey, IOcpTreeBaseService treeBaseService){
        if(StringUtils.isEmpty(innerCodeKey)){
            //TODO innerCode Exception
            return null;
        }

        StringRedisTemplate redisTemplate = OcpSpringUtil.getBean(StringRedisTemplate.class);
        String tenantId= InvocationInfoProxy.getTenantId();
        if(StringUtils.isNotEmpty(tenantId)){
            innerCodeKey+=("."+tenantId);
        }
        innerCodeKey+=("."+INCR_KEY_POSTFIX);
        String innerCode;
        Long innerValue = null;
        try{
            lock.lock();
            innerValue=redisTemplate.opsForValue().increment(innerCodeKey,1);
            logger.debug("innerValue:{}",innerValue);
            logger.debug("innerValue是否为0：{}",innerValue.compareTo(0L));
            if(innerValue.compareTo(1L)==0){
                //数据库查询出的值
                String dbReturnValue=treeBaseService.getMaxInnerCode();
                Long maxValue;
                if(StringUtils.isEmpty(dbReturnValue)){
                    maxValue=0L;
                }else{
                    maxValue= OcpNumberBaseConversionUtils.decode(dbReturnValue);
                }
                logger.debug("maxValue:{}",maxValue);
                if(maxValue.compareTo(innerValue)>0){
                    redisTemplate.opsForValue().set(innerCodeKey,maxValue+"");
                    innerValue=redisTemplate.opsForValue().increment(innerCodeKey,1);
                }
            }
        }catch (Exception e){
            logger.error("生成innercode错误：", e);
        }finally {
            lock.unlock();
        }
        if(innerValue==null){
            logger.error("生成innercode错误：");
        }else{
            innerCode= OcpNumberBaseConversionUtils.encode(innerValue);
            int length=innerCode.length();
            innerCode=pre[length-1]+innerCode;
            return innerCode;
        }

        return "innerCode";
    }
}
