package com.nbd.ocp.common.repository.tree.exception;
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


import com.nbd.ocp.core.exception.system.SystemException;

/**
 * 树形结构操作异常
 * @author jhb
 * //TODO 异常分类拆分
 */
public class TreeException extends SystemException {

    public static final  String TREE_PARENT_NULL="1";
    public static final  String TREE_PARENT_NULL_MSG="父节点不可为空";

    public static final  String TREE_PARENT_IS_SELF="2";
    public static final  String TREE_PARENT_IS_SELF_MSG="父节点不能是自身";

    public static final  String TREE_PARENT_IS_CHILDREN="3";
    public static final  String TREE_PARENT_IS_CHILDREN_MSG="父节点不能是自己的子节点";

    public static final  String TREE_HAS_CHILDREN_CANNOT_DELETE="4";
    public static final  String TREE_HAS_CHILDREN_CANNOT_DELETE_MSG="节点有子节点不允许删除";

    public static final  String TREE_CHANGE_2_SAME_PARENT="5";
    public static final  String TREE_CHANGE_2_SAME_PARENT_MSG="无效操作，移动前后父节点相同";


    public TreeException(String code, String msg) {
        super(code ,msg);
    }

    @Override
    public String errorCode() {
        return null;
    }


}
