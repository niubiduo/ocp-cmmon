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


import com.nbd.ocp.common.repository.base.IOcpBaseDo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jhb
 */
public class OcpBaseDaoUtils {

    /**
     * 生成jpa integer in sql
     * @param root root
     * @param criteriaBuilder criteriaBuilder
     * @param fieldName 表字段名称
     * @param itemList 包含的数据列表
     * @param <T> 对象应实体
     * @return sql对象
     */
    public static <T> Predicate  generateIntegerIn(Root<T> root,  CriteriaBuilder criteriaBuilder,String fieldName,List<Integer> itemList){
        if(itemList==null||itemList.size()<1){
            return null;
        }
        CriteriaBuilder.In<Integer> inPredicate = criteriaBuilder.in(root.get(fieldName).as(Integer.class));
        for(Integer i:itemList){
            inPredicate.value(i);
        }
        return inPredicate;
    }

    /**
     * 生成排序对象
     * @param sortMap column:direction
     * @return 排序对象
     */
    public static Sort generatedSort(Map<String,String> sortMap){
        Sort sort;
        if(sortMap!=null&&sortMap.size()>0){
            List<Sort.Order> orderList =new ArrayList<>();

            for(Map.Entry<String,String> entry:sortMap.entrySet()){
                String column=entry.getKey();
                String directionStr=entry.getValue();
                Sort.Direction direction;
                if(Sort.Direction.DESC.name().equalsIgnoreCase(directionStr)){
                    direction=Sort.Direction.DESC;
                }else{
                    direction=Sort.Direction.ASC;
                }
                Sort.Order sortOrder=new Sort.Order(direction,column);
                orderList.add(sortOrder);
            }
            sort=Sort.by(orderList);
            return sort;
        }else {
            return null;
        }
    }
    public static Pageable generatedPage(Integer pageIndex, Integer pageSize, Map<String,String> sortMap){
        Sort sort= OcpBaseDaoUtils.generatedSort(sortMap);
        if(sort==null){
            return PageRequest.of(pageIndex,pageSize );
        }else {
            return PageRequest.of(pageIndex,pageSize,sort );
        }

    }

    /**
     * 生成jpa String in sql
     * @param root root
     * @param criteriaBuilder criteriaBuilder
     * @param fieldName 表字段名称
     * @param itemList 包含的数据列表
     * @param <T> 对象应实体
     * @return sql对象
     */
    public static <T> Predicate  generateStringIn(Root<T> root,  CriteriaBuilder criteriaBuilder,String fieldName,List<String> itemList){
        if(itemList==null||itemList.size()<1){
            return null;
        }
        CriteriaBuilder.In<String> inPredicate = criteriaBuilder.in(root.get(fieldName).as(String.class));
        for(String i:itemList){
            inPredicate.value(i);
        }
        return inPredicate;

    }

    /**
     * 根据传值进行条件过滤
     * @param root root
     * @param criteriaBuilder criteriaBuilder
     * @param searchQu 查询条件
     * @param parameters 查询值
     * @param <T> 对象应实体
     * @return sql对象
     */
    public static <T extends IOcpBaseDo> Predicate generateQuery(Root<T> root, CriteriaBuilder criteriaBuilder, String searchQu, Map<String,Object>  parameters) {
        return OcpPartTreeConverter.toIndexedQuery(root,  criteriaBuilder, searchQu,parameters,root.getJavaType());
    }


}
