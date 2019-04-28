package com.nbd.ocp.common.repository.crud;

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


import com.nbd.ocp.core.context.util.OcpSpringUtil;
import com.nbd.ocp.core.exception.system.ExistsDataException;
import com.nbd.ocp.common.repository.constant.OcpCrudBaseDoConstant;
import com.nbd.ocp.common.repository.utils.OcpGenericsUtils;
import com.nbd.ocp.core.request.OcpQueryPageBaseVo;
import com.nbd.ocp.core.utils.bean.OcpBeanCompareUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author jhb
 */
public interface IOcpCrudBaseServiceImpl<T extends IOcpCrudBaseDo,I extends IOcpCrudBaseDao> extends  IOcpCrudBaseService<T,I> {
    default I getCrudBaseDao(){
        return (I) OcpSpringUtil.getBean(OcpGenericsUtils.getDaoSuperClassGenericsType(getClass(), IOcpCrudBaseDao.class));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    default T save(T t) {
        if(t==null|| StringUtils.isNotEmpty(t.getId())){
            throw new ExistsDataException("数据已存在不允许新增","数据已存在不允许新增");
        }
        beforeSave(t);
        T r= (T) getCrudBaseDao().save(t);
        afterSave(t,r);
        return r;
    }
    default void beforeSave(T t){}
    default void afterSave(T tSubmit,T tSaved){}
    @Override
    @javax.transaction.Transactional(rollbackOn = Exception.class)
    default  List<T> saveAll(List<T> list){
        if(list==null|list.size()<1){
            return null;
        }
        beforeSaveAll(list);
        List<T> listDB=getCrudBaseDao().saveAll(list);
        afterSaveAll(list,listDB);
        return listDB;
    }
    default void beforeSaveAll(List<T> list){}
    default void afterSaveAll(List<T> listSubmit,List<T> listSaved){}

    @Override
    @Transactional(rollbackOn = Exception.class)
    default  void deleteById(String id) {
        T doDB=getById(id);
        beforeDeleteById(id,doDB);
        getCrudBaseDao().delete(doDB);
        afterDeleteById(id,doDB);
    }
    default void beforeDeleteById(String id,T t){}
    default void afterDeleteById(String id,T t){}

    @Override
    @Transactional(rollbackOn = Exception.class)
    default T updateSelective(T t) {
        T userDoDB=getById(t.getId());
        beforeUpdate(t,userDoDB);
        OcpBeanCompareUtils.combineSydwCore(t,userDoDB);
        T r = (T) getCrudBaseDao().save(userDoDB);
        afterUpdate(t,userDoDB,r);
        return r;
    }
    default void beforeUpdate(T tSubmit,T tInDb){}

    default void afterUpdate(T tSubmit,T tInDb,T tSaved){}

    @Override
    default T getById(String id) {
        Optional<T> optionalT =getCrudBaseDao().findOne((Specification<T>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(OcpCrudBaseDoConstant.DB_COLUMN_ID).as(String.class),id));
            Predicate predicate=beforeGetById(root,criteriaBuilder,id);
            if(predicate!=null){
                predicates.add(predicate);
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        T r=null;
        if(optionalT.isPresent()){
            r=optionalT.get();
        }
        afterGetById(r);
        return r;
    }
    default Predicate beforeGetById(Root<T> root, CriteriaBuilder criteriaBuilder, String id){
        return null;
    }
    default void afterGetById(T r){}

    @Override
    default Page<T> page(final OcpQueryPageBaseVo ocpQueryPageBaseVo) {
        beforePage(ocpQueryPageBaseVo);
        Page<T> page= getCrudBaseDao().page(ocpQueryPageBaseVo);
        afterPage(page,ocpQueryPageBaseVo);
        return page;
    }
    default  void beforePage(OcpQueryPageBaseVo ocpQueryPageBaseVo){}
    default  void afterPage(Page<T> page,OcpQueryPageBaseVo ocpQueryPageBaseVo){ }


    @Override
    default List<T> list(OcpQueryPageBaseVo queryBaseVo) {
        beforeList(queryBaseVo);
        List<T> list= (List<T>) getCrudBaseDao().list(queryBaseVo);
        afterList(list,queryBaseVo);
        return list;
    }

    default  void beforeList(OcpQueryPageBaseVo ocpQueryPageBaseVo){}
    default  void afterList(List<T> list,OcpQueryPageBaseVo queryBaseVo){}

}
