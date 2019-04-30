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

import com.nbd.ocp.common.repository.base.IOcpBaseController;
import com.nbd.ocp.common.repository.utils.OcpGenericsUtils;
import com.nbd.ocp.core.context.util.OcpSpringUtil;
import com.nbd.ocp.common.repository.tree.request.OcpTreeOcpQueryBaseVo;
import com.nbd.ocp.core.request.OcpQueryPageBaseVo;
import com.nbd.ocp.core.response.OcpJsonResponse;
import com.nbd.ocp.common.busilog.anotation.LogConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author jhb
 */
public interface IOcpTreeBaseController<T extends IOcpTreeBaseDo,I extends IOcpTreeBaseService>  extends IOcpBaseController<T,I> {

    Logger logger = LoggerFactory.getLogger(IOcpTreeBaseController.class);

    default I getTreeBaseService(){
        return (I) OcpSpringUtil.getBean(OcpGenericsUtils.getControllerSuperClassGenericsType(getClass(), IOcpTreeBaseService.class));
    }
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @LogConfig(operationCode="001")
    default OcpJsonResponse add(@RequestBody T t) {
        T r= (T) getTreeBaseService().save(t);
        return OcpJsonResponse.success("新增成功",r);
    }
    @RequestMapping(value = "{id}",method = RequestMethod.DELETE)
    @ResponseBody
    @LogConfig(operationCode="002")
    default OcpJsonResponse delete(@PathVariable("id")  String id) {
        getTreeBaseService().deleteById(id);
        return OcpJsonResponse.success("删除成功");
    }
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    @LogConfig(operationCode="003")
    default OcpJsonResponse update(@RequestBody T t) {
        T r= (T) getTreeBaseService().updateSelective(t);
        return OcpJsonResponse.success("更新成功",r);
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ResponseBody
    default OcpJsonResponse getById(@PathVariable("id")  String id) {
        T r= (T) getTreeBaseService().getById( id);
        return OcpJsonResponse.success("查询成功",r);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    default OcpJsonResponse page(OcpQueryPageBaseVo ocpQueryPageBaseVo) {
        Page<T> r=getTreeBaseService().page(ocpQueryPageBaseVo);
        return OcpJsonResponse.success("查询成功",r);
    }
    @RequestMapping(value = "tree", method = RequestMethod.GET)
    @ResponseBody
    default OcpJsonResponse listTree(OcpTreeOcpQueryBaseVo treeQueryBaseVo) {
        try {
            List<T> r =getTreeBaseService().listTree(treeQueryBaseVo);
            return OcpJsonResponse.success("查询成功",r);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return OcpJsonResponse.failed();
        }
    }



}
