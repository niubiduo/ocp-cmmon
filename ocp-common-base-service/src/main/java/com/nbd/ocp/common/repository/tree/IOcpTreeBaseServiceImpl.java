package com.nbd.ocp.common.repository.tree;
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

import com.nbd.ocp.common.repository.base.IOcpBaseServiceImpl;
import com.nbd.ocp.common.repository.constant.OcpCrudBaseDoConstant;
import com.nbd.ocp.common.repository.tree.constant.OcpTreeBaseConstant;
import com.nbd.ocp.common.repository.tree.exception.TreeException;
import com.nbd.ocp.common.repository.tree.request.OcpTreeOcpQueryBaseVo;
import com.nbd.ocp.common.repository.tree.utils.OcpInnerCodeUtils;
import com.nbd.ocp.common.repository.tree.utils.OcpTreeUtils;
import com.nbd.ocp.common.repository.utils.OcpGenericsUtils;
import com.nbd.ocp.core.context.util.OcpSpringUtil;
import com.nbd.ocp.core.exception.system.ExistsDataException;
import com.nbd.ocp.core.request.OcpQueryPageBaseVo;
import com.nbd.ocp.core.utils.bean.OcpBeanCompareUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @author jhb
 */
public  interface IOcpTreeBaseServiceImpl<T extends IOcpTreeBaseDo,I extends IOcpTreeBaseDao> extends IOcpTreeBaseService<T,I>, IOcpBaseServiceImpl<T,I> {

    /**
     * 获取对应的dao
     * @return 对应实体的dao
     */
    default I getTreeBaseDao(){
        return (I) OcpSpringUtil.getBean(OcpGenericsUtils.getDaoSuperClassGenericsType(getClass(), IOcpTreeBaseDao.class));
    }
    String getInnerCodeKey();

    /**
     * 保存树状结构：
     * 1、不存在pid。则默认为虚拟根节点
     * 2、检查父节点是否存在。
     * 3、设置级联树的结构码
     * @param t
     * @return
     */
    @Override
    @javax.transaction.Transactional(rollbackOn = Exception.class)
    default T save(T t){
        if(t==null|| StringUtils.isNotEmpty(t.getId())){
            throw new ExistsDataException("数据已存在不允许新增","数据已存在不允许新增");
        }
        beforeSave(t);
        /** 获取内部码 */
        t.setInnerCode(OcpInnerCodeUtils.getRedisKey(getInnerCodeKey(),this));
        String cascadeInnerCode;
        /**根节点为虚拟节点默认设置为root*/
        if(StringUtils.isEmpty(t.getPid())|| OcpTreeBaseConstant.TREE_ROOT_ID.equals(t.getPid())){
            t.setPid(OcpTreeBaseConstant.TREE_ROOT_ID);
            cascadeInnerCode=t.getInnerCode();
        }else{
            IOcpTreeBaseDo treeBaseDoParent = (IOcpTreeBaseDo) getTreeBaseDao().findById(t.getPid()).get();
            if(treeBaseDoParent!=null){
                cascadeInnerCode=treeBaseDoParent.getCascadeInnerCode()+t.getInnerCode();
            }else {
                /** 父节点不存在抛出异常*/
                throw new TreeException(TreeException.TREE_PARENT_IS_CHILDREN,TreeException.TREE_PARENT_IS_CHILDREN_MSG);
            }
        }
        /** 设置级联内部码表示树的结构 */
        t.setCascadeInnerCode(cascadeInnerCode);
        T r= (T) getTreeBaseDao().save(t);
        afterSave(r,t);
        return r;
    }
    default void beforeSave(T t){}
    default void afterSave(T tSaved,T tSubmit){}
    /**
     * 批量保存。循环调用save方法。 TODO 开启事务采用批量提交。
     * @param list
     * @return
     */
    @Override
    @javax.transaction.Transactional(rollbackOn = Exception.class)
    default  List<T> saveAll(List<T> list) {
        if(list==null|list.size()<1){
            return null;
        }
        beforeSaveAll(list);
        List<T> result=new ArrayList<>();
        for(T t:list){
            result.add(save(t));
        }
        afterSaveAll(list,result);
        return result;
    }

    default void beforeSaveAll(List<T> list){}
    default void afterSaveAll(List<T> listSubmit,List<T> listSaved){}

    @Override
    @javax.transaction.Transactional(rollbackOn = Exception.class)
    default  void deleteById(String id) {
        T doDB=getById(id);
        beforeDeleteById(id,doDB);
        if(doDB==null){
            return ;
        }else if(hasChildren(doDB.getCascadeInnerCode())){
            throw new TreeException(TreeException.TREE_HAS_CHILDREN_CANNOT_DELETE,TreeException.TREE_HAS_CHILDREN_CANNOT_DELETE_MSG);
        }else{
            getTreeBaseDao().delete(doDB);
        }
        afterDeleteById(id,doDB);
    }
    default void beforeDeleteById(String id,T t){}
    default void afterDeleteById(String id,T t){}
    /**
     * 更新节点信息
     * 1、
     * @param t
     * @return
     */
    @Override
    @javax.transaction.Transactional(rollbackOn = Exception.class)
    default T updateSelective(T t) {
        if(t.getId().equals(t.getPid())){
            throw new TreeException(TreeException.TREE_PARENT_IS_SELF,TreeException.TREE_PARENT_IS_SELF_MSG);
        }
        T treeDoDb=getById(t.getId());
        beforeUpdate(t,treeDoDb);
        String cascadeInnerCode;
        /**根节点为虚拟节点默认设置为root*/
        if(StringUtils.isEmpty(t.getPid())|| OcpTreeBaseConstant.TREE_ROOT_ID.equals(t.getPid())){
            t.setPid(OcpTreeBaseConstant.TREE_ROOT_ID);
            /** 节点移动到根节点下、innerCode 和 cascadeInnerCode相同 */
            cascadeInnerCode=t.getInnerCode();
            t.setCascadeInnerCode(cascadeInnerCode);
            /** 移动子节点、根据新的父节点设置CascadeInnerCode **/
            moveAllChildren(treeDoDb.getCascadeInnerCode(),t.getCascadeInnerCode());
        /**更新节点信息操作无需移动子节点*/
        }else if(t.getPid().equals(treeDoDb.getPid())){
        }else{
            IOcpTreeBaseDo treeBaseDoParent =  (IOcpTreeBaseDo) getTreeBaseDao().findById(t.getPid()).get();
            /** 判断父节点不存在 */
            if(treeBaseDoParent==null){
                throw new TreeException(TreeException.TREE_PARENT_NULL,TreeException.TREE_PARENT_NULL_MSG);
            }
            /** 不可移动父节点移动到子节点下 */
            if(treeBaseDoParent.getCascadeInnerCode().startsWith(treeDoDb.getCascadeInnerCode())){
                throw new TreeException(TreeException.TREE_PARENT_IS_CHILDREN,TreeException.TREE_PARENT_IS_CHILDREN_MSG);
            }
            /** 移动子节点、根据新的父节点设置CascadeInnerCode **/
            cascadeInnerCode=treeDoDb.getCascadeInnerCode();
            t.setCascadeInnerCode(treeBaseDoParent.getCascadeInnerCode()+treeDoDb.getInnerCode());
            moveAllChildren(cascadeInnerCode,t.getCascadeInnerCode());
        }
        OcpBeanCompareUtils.combineSydwCore(t,treeDoDb);
        T r = (T) getTreeBaseDao().save(treeDoDb);
        afterUpdate(t,treeDoDb,r);
        return r;
    }
    default void beforeUpdate(T tSubmit,T tInDb){}
    default void afterUpdate(T tSubmit,T tInDb,T tSaved){}


    @Override
    default T getById(String id) {
        Optional<T> optionalT =getTreeBaseDao().findOne((Specification<T>) (root, criteriaQuery, criteriaBuilder) -> {
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
        Page<T> page= getTreeBaseDao().page(ocpQueryPageBaseVo);
        afterPage(page,ocpQueryPageBaseVo);
        return page;
    }
    default  void beforePage(OcpQueryPageBaseVo ocpQueryPageBaseVo){}
    default  void afterPage(Page<T> page,OcpQueryPageBaseVo ocpQueryPageBaseVo){ }

    @Override
    default List<T> list(OcpQueryPageBaseVo queryBaseVo) {
        beforeList(queryBaseVo);
        List<T> list= (List<T>) getTreeBaseDao().list(queryBaseVo);
        afterList(list,queryBaseVo);
        return list;
    }
    default  void beforeList(OcpQueryPageBaseVo ocpQueryPageBaseVo){}
    default  void afterList(List<T> list,OcpQueryPageBaseVo queryBaseVo){}

    /**
     * 获取所有已生成的innercode中的最大值
     * @return 最大值的64进制
     */
    @Override
    default String getMaxInnerCode(){
        Sort sort= new Sort(Sort.Direction.DESC,"innerCode");
        Pageable pageable = PageRequest.of(0,1,sort);
        Page<T> page= getTreeBaseDao().findAll((Specification<T>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },pageable);

        if(page.getTotalElements()>0){
            return page.getContent().get(0).getInnerCode();
        }else{
            return "0";
        }

    }

    /**
     * 移动所有的子节点到新的父节点下
     * @param cascadeInnerCodeParent 父节点修改前的 cascadeInnerCode
     * @param cascadeInnerCodeParentNew 父节点修改后的 cascadeInnerCode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    default void moveAllChildren(String cascadeInnerCodeParent, String cascadeInnerCodeParentNew){
        if(StringUtils.isEmpty(cascadeInnerCodeParent)||StringUtils.isEmpty(cascadeInnerCodeParentNew)){
            //TODO 抛出空指针异常
        }
        List<T>  treeBaseDos=listAllChildren(cascadeInnerCodeParent);
        if(treeBaseDos!=null&&treeBaseDos.size()>0){
            /** 生成新的级联码 */
            for(T t:treeBaseDos){
                t.setCascadeInnerCode(cascadeInnerCodeParentNew+t.getCascadeInnerCode().substring(cascadeInnerCodeParent.length()));
            }
            /** 更新所有的级联码 */
            getTreeBaseDao().saveAll(treeBaseDos);
        }
    }

    /**
     * 判断是否有子节点
     * @param cascadeInnerCodeParent 父节点id
     * @return
     */
    @Override
    default boolean hasChildren(String cascadeInnerCodeParent){
        List<T>  treeBaseDos=listAllChildren(cascadeInnerCodeParent);
        return treeBaseDos!=null&&treeBaseDos.size()>0;
    }

    default List<T> listAllChildren(String cascadeInnerCodeParent){
        Sort sort= Sort.by("cascadeInnerCode");
        List<T>  treeBaseDos=getTreeBaseDao().findAll((Specification<T>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.like(root.get("cascadeInnerCode"),cascadeInnerCodeParent+"%"));
            predicates.add(criteriaBuilder.notEqual(root.get("cascadeInnerCode"),cascadeInnerCodeParent));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },sort);
        return treeBaseDos;
    }

    @Override
    default List<T> treeAllChildrenBysCascade(String cascadeInnerCodeParent){
        List<T> list=listAllChildren(cascadeInnerCodeParent);
        return OcpTreeUtils.list2Tree(list);
    }



    @Override
    default  List<T> listTree(OcpTreeOcpQueryBaseVo treeQueryBaseVo){
        String pid=StringUtils.isEmpty(treeQueryBaseVo.getPid())? OcpTreeBaseConstant.TREE_ROOT_ID:treeQueryBaseVo.getPid();
        List<T> list=listAllChildrenById(pid);
        return OcpTreeUtils.list2Tree(list);
    }


    default List<T> listAllChildrenById(String id){
        Sort sort= Sort.by("cascadeInnerCode");
        String cascadeInnerCodeParent=null;
        /** id对应的不是根节点则初始化父节点的cascadeInnerCode */
        if(!OcpTreeBaseConstant.TREE_ROOT_ID.equals(id)){
            Optional<T> optionalT=getTreeBaseDao().findOne((Specification<T>) (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("id"),id));
                return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            });
            if(!optionalT.isPresent()){
                return null;
            }else {

            }
            cascadeInnerCodeParent=optionalT.get().getCascadeInnerCode();
        }
        List<T> result;
        /** 根节点则返回整个树，查询所有的子节点 */
        if(StringUtils.isEmpty(cascadeInnerCodeParent)){
            result= getTreeBaseDao().findAll((Specification<T>) (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            },sort);
        }else{
            result= listAllChildren(cascadeInnerCodeParent);
        }

        return result;
    }


    default void setPTitle(T r){
        if(r==null||StringUtils.isEmpty(r.getPid())){
            return;
        }
        if(OcpTreeBaseConstant.TREE_ROOT_ID.equals(r.getPid())){
            r.setPTitle(OcpTreeBaseConstant.TREE_ROOT_NAME);
            return;
        }
        T treeBaseDoParent =  getById(r.getPid()) ;
        if(treeBaseDoParent==null){
            return;
        }
        r.setPTitle(treeBaseDoParent.getTitle());
    }
    default void setPTitle(List<T> list){
        List<String> pIds=new ArrayList<>();
        for(T treeBaseDo:list){
            pIds.add(treeBaseDo.getPid());
        }
        List<T> treeBaseDoList=getTreeBaseDao().findByIdIn(pIds);
        Map<String,T> treeBaseDoMap=new HashMap<>();
        for(T treeBaseDo:treeBaseDoList){
            treeBaseDoMap.put(treeBaseDo.getId(),treeBaseDo);
        }
        for(T treeBaseDo:list){
            String pid=treeBaseDo.getPid();
            if(OcpTreeBaseConstant.TREE_ROOT_ID.equals(pid)){
                treeBaseDo.setPTitle(OcpTreeBaseConstant.TREE_ROOT_NAME);
            }else {
                T treeBaseDoParent=treeBaseDoMap.get(pid);
                treeBaseDo.setPTitle(treeBaseDoParent.getTitle());
            }
        }
    }

}
