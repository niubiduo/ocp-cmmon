package com.nbd.ocp.common.repository.utils;
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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jhb
 */
public class OcpGenericsUtils {
    private static final Logger logger= LoggerFactory.getLogger(OcpGenericsUtils.class);
    private static final ConcurrentHashMap<String,String> SERVICE_NAME= new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String,String> DAO_NAME= new ConcurrentHashMap<>();
    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     * @param realClazz 声明泛型的类  如  BaseService<T0,T1>
     * @return 泛型的class
     */
    public static String getControllerSuperClassGenericsType(final Class realClazz, final Class targetInterfaceClazz) {
        String typeName=realClazz.getTypeName();
        String daoTypeName=SERVICE_NAME.get(typeName);
        if(StringUtils.isNotEmpty(daoTypeName)){
            return daoTypeName;
        }
        Type[] types = realClazz.getGenericInterfaces();
        try {
            for(Type type:types){
                if(type instanceof  ParameterizedType){
                    ParameterizedType parameterized = (ParameterizedType) type;
                    Type[] typeAry=parameterized.getActualTypeArguments();
                    for(Type typeSuper:typeAry){
                        typeName=typeSuper.getTypeName();
                        if(targetInterfaceClazz.isAssignableFrom(Class.forName(typeName))){
                            SERVICE_NAME.put(typeName,typeName);
                            return typeName;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(),e);
        }
        return null;

    }

    public static String getDaoSuperClassGenericsType(final Class realClazz, final Class targetInterfaceClazz) {
        String typeName=realClazz.getTypeName();
        String daoTypeName=DAO_NAME.get(typeName);
        if(StringUtils.isNotEmpty(daoTypeName)){
            return daoTypeName;
        }
        Class[] superClass = realClazz.getInterfaces();
        for(Class clazzSuper:superClass){
            Type[] typeSupers=clazzSuper.getGenericInterfaces();
            for(Type typeSuper:typeSupers){
                if(typeSuper instanceof  ParameterizedType){
                    ParameterizedType parameterized = (ParameterizedType) typeSuper;
                    Type[] typeAry=parameterized.getActualTypeArguments();
                    for(Type type:typeAry){
                        try {
                            daoTypeName=type.getTypeName();
                            if(targetInterfaceClazz.isAssignableFrom(Class.forName(daoTypeName))){
                                DAO_NAME.put(typeName,daoTypeName);
                                return daoTypeName;
                            }
                        } catch (ClassNotFoundException e) {
                            logger.error(e.getMessage(),e);
                        }
                    }
                }

            }
        }
        return null;
    }
}
