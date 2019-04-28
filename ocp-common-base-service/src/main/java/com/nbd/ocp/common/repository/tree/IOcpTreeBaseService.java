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

import com.nbd.ocp.common.repository.base.IOcpBaseService;
import com.nbd.ocp.common.repository.multiTenancy.discriminator.annotations.OcpMultiTenancy;
import com.nbd.ocp.common.repository.tree.request.OcpTreeOcpQueryBaseVo;
import com.nbd.ocp.core.request.OcpQueryPageBaseVo;
import org.springframework.data.domain.Page;

import java.util.List;


/**
 * @author jhb
 */
@org.springframework.transaction.annotation.Transactional(readOnly = true,rollbackFor = Exception.class)
@OcpMultiTenancy
public  interface IOcpTreeBaseService<T extends IOcpTreeBaseDo,I extends IOcpTreeBaseDao> extends IOcpBaseService<T,I> {


    T save(T treeBaseDo) ;

    List<T> saveAll(List<T> list);

    T updateSelective(T treeBaseDo);

    List<T> treeAllChildrenBysCascade(String cascadeInnerCodeParent);

    List<T> listTree(OcpTreeOcpQueryBaseVo treeQueryBaseVo);

    void deleteById(String id);

    void moveAllChildren(String cascadeInnerCode, String cascadelInnerCodeParent);

    boolean hasChildren(String cascadeInnerCodeParent);

    String getMaxInnerCode();

    T getById(String id);


    Page<T> page(OcpQueryPageBaseVo ocpQueryPageBaseVo);

    List<T> list( OcpQueryPageBaseVo queryBaseVo);


}
