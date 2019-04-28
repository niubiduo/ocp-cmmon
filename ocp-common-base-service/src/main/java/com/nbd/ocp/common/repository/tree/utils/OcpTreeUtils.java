package com.nbd.ocp.common.repository.tree.utils;

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


import com.nbd.ocp.common.repository.tree.IOcpTreeBaseDo;
import com.nbd.ocp.common.repository.tree.constant.OcpTreeBaseConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jhb
 */
public class OcpTreeUtils {
    private  static Logger logger = LoggerFactory.getLogger(OcpTreeUtils.class);

    public static  <T extends IOcpTreeBaseDo> List<T> list2Tree(List<T> list){
        if(list==null||list.size()<1){
            return null;
        }

        T result= null;
        try {
            result = (T) list.get(0).getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Map<String,T> map    =new HashMap<>(list.size());

        for(T t:list){
            map.put(t.getCascadeInnerCode(),t);
            String cascadeInnerCode=t.getCascadeInnerCode();
            String cascadeInnerCodeParent=cascadeInnerCode.substring(0,cascadeInnerCode.length()-6);
            if(cascadeInnerCode.length()==6||map.get(cascadeInnerCodeParent)==null){
                t.setPTitle(OcpTreeBaseConstant.TREE_ROOT_NAME);
                result.getChildren().add(t);
            }else{
                T treeParent=map.get(cascadeInnerCodeParent);
                t.setPTitle(treeParent.getTitle());
                treeParent.getChildren().add(t);
            }

        }
        return result.getChildren();
    }

}
