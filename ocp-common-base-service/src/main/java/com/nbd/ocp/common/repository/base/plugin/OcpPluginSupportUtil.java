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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 判断当前plugin是否使用当前执行的service
 * @author jhb
 */
public class OcpPluginSupportUtil {
    public static boolean support(Class<?> c,Class<?> t) {
        Class[] superClass = c.getInterfaces();
        for(Class clazz:superClass){
            Type[] types = clazz.getGenericInterfaces();
            for(Type type:types){
                if(type instanceof  ParameterizedType){
                    ParameterizedType parameterized = (ParameterizedType) type;
                    Type[] typeAry=parameterized.getActualTypeArguments();
                    if(typeAry!=null& typeAry.length>0){
                        Class targetClazz= (Class) parameterized.getActualTypeArguments()[0];
                        if(t.isAssignableFrom(targetClazz)){
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }
    public static String getBaseDoName(Class<?> c,Class<?> t) {
        Type[] types = c.getGenericInterfaces();
        for(Type type:types){
            if(type instanceof  ParameterizedType){
                ParameterizedType parameterized = (ParameterizedType) type;
                Type[] typeAry=parameterized.getActualTypeArguments();
                if(typeAry!=null& typeAry.length>0){
                    Class clazz= (Class) parameterized.getActualTypeArguments()[0];
                    if(t.isAssignableFrom(clazz)){
                        return clazz.getName();
                    }
                }
            }

        }
        return null;
    }
}
