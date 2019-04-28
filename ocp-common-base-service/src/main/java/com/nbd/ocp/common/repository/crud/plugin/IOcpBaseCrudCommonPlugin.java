package com.nbd.ocp.common.repository.crud.plugin;
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

import com.nbd.ocp.common.repository.base.plugin.IOcpBaseCommonPlugin;
import com.nbd.ocp.common.repository.crud.IOcpCrudBaseDao;
import com.nbd.ocp.common.repository.crud.IOcpCrudBaseDo;
import com.nbd.ocp.core.request.OcpQueryPageBaseVo;
import org.springframework.data.domain.Page;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author jhb
 */
public interface IOcpBaseCrudCommonPlugin<T extends IOcpCrudBaseDo,I extends IOcpCrudBaseDao> extends IOcpBaseCommonPlugin<T,I> {



    void beforeSave(I baseDao, T t);

    void afterSave(I baseDao, T tSaved, T tSubmit);

    void beforeSaveAll(I baseDao, List<T> list);

    void afterSaveAll(I baseDao, List<T> listSaved, List<T> listSubmit);


    void beforeUpdateSelective(I baseDao, T t);

    void afterUpdateSelective(I baseDao, T tUpdated, T tSubmit);



    void beforeDelete(I baseDao, T userDoDB);

    void afterDelete(I baseDao, T userDoDB);


    Predicate beforeGetById(I baseDao, Root<T> root, CriteriaBuilder criteriaBuilder, String id);

    void afterGetById(I baseDao, T r);

    Predicate beforePage(I baseDao, Root<T> root, CriteriaBuilder criteriaBuilder, OcpQueryPageBaseVo ocpQueryPageBaseVo);

    void afterPage(I baseDao, Page<T> page, OcpQueryPageBaseVo ocpQueryPageBaseVo);

    Predicate beforeList(I baseDao, Root<T> root, CriteriaBuilder criteriaBuilder, OcpQueryPageBaseVo queryBaseVo);

    void afterList(I baseDao, List<T> list, OcpQueryPageBaseVo queryBaseVo);



}
